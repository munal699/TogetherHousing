package org.example.togetherhousing;

import org.example.togetherhousing.controller.ImageController;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.PropertyStatus;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.PropertyRepository;
import org.example.togetherhousing.repository.userRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class DatabaseImageStorageTest {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private ImageController imageController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseImageStorageAndRetrievalFlow() {
        System.out.println("========== STEP 1: VERIFY TIDB COLUMNS FOR BLOB STORAGE ==========");
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'property' AND COLUMN_NAME IN ('image_data', 'image_content_type')"
        );
        System.out.println("Found TiDB columns: " + columns);
        Assertions.assertFalse(columns.isEmpty(), "Columns image_data / image_content_type must exist in property table!");

        System.out.println("========== STEP 2: TEST SAVING IMAGE BYTES INTO DATABASE ==========");
        UserTbl seller = userRepository.findAll().stream().findFirst().orElseGet(() -> {
            UserTbl u = new UserTbl();
            u.setFullname("Test Seller");
            u.setEmail("testseller_" + System.currentTimeMillis() + "@togetherhousing.com");
            u.setPassword("testpass");
            u.setRole("SELLER");
            return userRepository.save(u);
        });

        // Sample JPEG binary bytes
        byte[] testImageBytes = new byte[] {
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0,
                0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01
        };

        Property prop = new Property();
        prop.setTitle("Automated Verification Villa");
        prop.setLocation("Kathmandu Test Valley");
        prop.setPrice(4500000.0);
        prop.setSeller(seller);
        prop.setStatus(PropertyStatus.APPROVED);
        prop.setImageData(testImageBytes);
        prop.setImageContentType("image/jpeg");

        Property saved = propertyRepository.save(prop);
        Assertions.assertNotNull(saved.getId(), "Saved property ID must not be null");
        System.out.println("Saved Property ID: " + saved.getId());

        System.out.println("========== STEP 3: VERIFY BYTES PERSISTED IN TIDB VIA SQL ==========");
        Integer byteLength = jdbcTemplate.queryForObject(
                "SELECT LENGTH(image_data) FROM property WHERE id = ?",
                Integer.class,
                saved.getId()
        );
        System.out.println("TiDB reports image_data length in bytes = " + byteLength);
        Assertions.assertNotNull(byteLength, "Database image_data length must not be null");
        Assertions.assertEquals(testImageBytes.length, byteLength.intValue(), "Byte count stored in TiDB must match uploaded image bytes!");

        System.out.println("========== STEP 4: VERIFY BACKEND IMAGE STREAMING ENDPOINT ==========");
        ResponseEntity<byte[]> response = imageController.getPropertyImage(saved.getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Endpoint must return 200 OK");
        Assertions.assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType(), "Content-Type header must be image/jpeg");
        Assertions.assertNotNull(response.getBody(), "Response body must contain image bytes");
        Assertions.assertArrayEquals(testImageBytes, response.getBody(), "Returned bytes must match the exact bytes saved in database!");
        System.out.println("Backend endpoint /property/image/" + saved.getId() + " successfully streamed database bytes!");

        System.out.println("========== STEP 5: VERIFY FALLBACK FOR MISSING/DEFAULT IMAGE ==========");
        ResponseEntity<byte[]> fallbackResponse = imageController.getPropertyImage(999999);
        Assertions.assertEquals(HttpStatus.OK, fallbackResponse.getStatusCode(), "Fallback endpoint must return 200 OK");
        Assertions.assertNotNull(fallbackResponse.getBody(), "Fallback body must not be null");
        Assertions.assertTrue(fallbackResponse.getBody().length > 0, "Fallback body must contain fallback image bytes");
        System.out.println("Fallback test passed! Default image served cleanly.");

        // Cleanup
        propertyRepository.deleteById(saved.getId());
        System.out.println("Test cleanup completed successfully.");
    }
}
