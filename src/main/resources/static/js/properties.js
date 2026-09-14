
// Authentic Nepalese Properties Database (52 Listings across Nepal)
// Features authentic Nepal land plots (Ghaderi), terraced hills, and Nepalese housing.
const sampleProperties = [
    // 1-10: Kathmandu Valley
    {
        id: 1,
        title: "Bhaktapur Thimi Modern Villa",
        location: "Thimi, Bhaktapur",
        lat: 27.6784, lng: 85.3853,
        price: 3000000, priceFormatted: "Rs 30,00,000",
        area: "1,000 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Housing", rating: 4.9, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 2,
        title: "Lalitpur Godawari Residency",
        location: "Godawari, Lalitpur",
        lat: 27.5957, lng: 85.3789,
        price: 4500000, priceFormatted: "Rs 45,00,000",
        area: "1,250 sq ft", type: "apartment", bedrooms: 2, bathrooms: 2,
        seller: "Munal Tamang", rating: 4.8, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 3,
        title: "Pokhara Lakeside 4 Aana Ghaderi",
        location: "Lakeside, Pokhara",
        lat: 28.2096, lng: 83.9556,
        price: 5200000, priceFormatted: "Rs 52,00,000",
        area: "4 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 5.0, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 4,
        title: "Baneshwor Community Housing",
        location: "Baneshwor, Kathmandu",
        lat: 27.6934, lng: 85.3364,
        price: 6800000, priceFormatted: "Rs 68,00,000",
        area: "2,000 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Together Housing", rating: 4.9, image: "images/community.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 5,
        title: "Jhamsikhel 5 Aana Residential Plot",
        location: "Jhamsikhel, Lalitpur",
        lat: 27.6745, lng: 85.3123,
        price: 5500000, priceFormatted: "Rs 55,00,000",
        area: "5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Rajesh Shrestha", rating: 4.7, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 6,
        title: "Boudha Peaceful Suburban House",
        location: "Boudha, Kathmandu",
        lat: 27.7215, lng: 85.3620,
        price: 4800000, priceFormatted: "Rs 48,00,000",
        area: "1,150 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Housing", rating: 4.8, image: "images/villa.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 7,
        title: "Suryabinayak 3.5 Aana Ghaderi",
        location: "Suryabinayak, Bhaktapur",
        lat: 27.6620, lng: 85.4210,
        price: 2800000, priceFormatted: "Rs 28,00,000",
        area: "3.5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Munal Tamang", rating: 4.9, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 8,
        title: "Budhanilkantha Hillside Villa",
        location: "Budhanilkantha, Kathmandu",
        lat: 27.7780, lng: 85.3610,
        price: 8500000, priceFormatted: "Rs 85,00,000",
        area: "2,500 sq ft", type: "house", bedrooms: 5, bathrooms: 4,
        seller: "Together Group", rating: 5.0, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 9,
        title: "Sanepa Heights Apartment",
        location: "Sanepa, Lalitpur",
        lat: 27.6820, lng: 85.3050,
        price: 6200000, priceFormatted: "Rs 62,00,000",
        area: "1,500 sq ft", type: "apartment", bedrooms: 3, bathrooms: 3,
        seller: "Sita Sharma", rating: 4.6, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 10,
        title: "Kirtipur 4 Aana Valley View Plot",
        location: "Kirtipur, Kathmandu",
        lat: 27.6670, lng: 85.2770,
        price: 3200000, priceFormatted: "Rs 32,00,000",
        area: "4 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Housing", rating: 4.8, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },

    // 11-20: Pokhara & Gandaki Region
    {
        id: 11,
        title: "Fewa Lake View House",
        location: "Lakeside, Pokhara",
        lat: 28.2120, lng: 83.9580,
        price: 7500000, priceFormatted: "Rs 75,00,000",
        area: "1,800 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Munal Tamang", rating: 4.9, image: "images/villa.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 12,
        title: "Begnas Lake 6 Aana Ghaderi",
        location: "Begnas, Pokhara",
        lat: 28.1750, lng: 84.0950,
        price: 2400000, priceFormatted: "Rs 24,00,000",
        area: "6 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.7, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 13,
        title: "Sarangkot Mountain View Villa",
        location: "Sarangkot, Pokhara",
        lat: 28.2430, lng: 83.9480,
        price: 8900000, priceFormatted: "Rs 89,00,000",
        area: "2,200 sq ft", type: "house", bedrooms: 4, bathrooms: 4,
        seller: "Together Housing", rating: 5.0, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 14,
        title: "Batulechaur 5 Aana Terraced Plot",
        location: "Batulechaur, Pokhara",
        lat: 28.2580, lng: 83.9850,
        price: 3100000, priceFormatted: "Rs 31,00,000",
        area: "5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Rajesh Shrestha", rating: 4.8, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 15,
        title: "Hemja Highway 8 Aana Ghaderi",
        location: "Hemja, Kaski",
        lat: 28.2720, lng: 83.9350,
        price: 4200000, priceFormatted: "Rs 42,00,000",
        area: "8 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.6, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 16,
        title: "Pokhara Heights Apartment",
        location: "New Road, Pokhara",
        lat: 28.2150, lng: 83.9880,
        price: 4900000, priceFormatted: "Rs 49,00,000",
        area: "1,200 sq ft", type: "apartment", bedrooms: 2, bathrooms: 2,
        seller: "Together Housing", rating: 4.8, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 17,
        title: "Damauli Riverfront 5 Aana Plot",
        location: "Damauli, Tanahun",
        lat: 27.9730, lng: 84.2750,
        price: 1800000, priceFormatted: "Rs 18,00,000",
        area: "5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Sita Sharma", rating: 4.5, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 18,
        title: "Gorkha Heritage Residence",
        location: "Gorkha Bazaar, Gorkha",
        lat: 28.0050, lng: 84.6280,
        price: 3600000, priceFormatted: "Rs 36,00,000",
        area: "1,400 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Munal Tamang", rating: 4.7, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 19,
        title: "Palpa Tansen View House",
        location: "Tansen, Palpa",
        lat: 27.8670, lng: 83.5480,
        price: 3300000, priceFormatted: "Rs 33,00,000",
        area: "1,300 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Group", rating: 4.8, image: "images/villa.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 20,
        title: "Syangja Green Valley 6 Aana Plot",
        location: "Putalibazar, Syangja",
        lat: 28.0950, lng: 83.8750,
        price: 2100000, priceFormatted: "Rs 21,00,000",
        area: "6 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Housing", rating: 4.6, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },

    // 21-30: Chitwan & Terai Region
    {
        id: 21,
        title: "Narayangarh 10 Aana Ghaderi",
        location: "Narayangarh, Chitwan",
        lat: 27.6850, lng: 84.4320,
        price: 4500000, priceFormatted: "Rs 45,00,000",
        area: "10 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Housing", rating: 4.9, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 22,
        title: "Sauraha Green Resort Villa",
        location: "Sauraha, Chitwan",
        lat: 27.5830, lng: 84.4950,
        price: 6500000, priceFormatted: "Rs 65,00,000",
        area: "1,900 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Munal Tamang", rating: 5.0, image: "images/community.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 23,
        title: "Butwal Traffic Chowk Apartment",
        location: "Traffic Chowk, Butwal",
        lat: 27.7000, lng: 83.4500,
        price: 3800000, priceFormatted: "Rs 38,00,000",
        area: "1,100 sq ft", type: "apartment", bedrooms: 2, bathrooms: 2,
        seller: "Rajesh Shrestha", rating: 4.7, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 24,
        title: "Bhairahawa 8 Aana Ghaderi",
        location: "Bhairahawa, Rupandehi",
        lat: 27.5050, lng: 83.4520,
        price: 3900000, priceFormatted: "Rs 39,00,000",
        area: "8 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.8, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 25,
        title: "Lumbini Peace Colony Villa",
        location: "Lumbini, Rupandehi",
        lat: 27.4830, lng: 83.2750,
        price: 4200000, priceFormatted: "Rs 42,00,000",
        area: "1,500 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Housing", rating: 4.9, image: "images/community.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 26,
        title: "Hetauda 7 Aana Commercial Plot",
        location: "Hetauda, Makwanpur",
        lat: 27.4280, lng: 85.0320,
        price: 2600000, priceFormatted: "Rs 26,00,000",
        area: "7 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Sita Sharma", rating: 4.6, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 27,
        title: "Birgunj Bypass 10 Aana Ghaderi",
        location: "Bypass, Birgunj",
        lat: 27.0120, lng: 84.8780,
        price: 4800000, priceFormatted: "Rs 48,00,000",
        area: "10 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.7, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 28,
        title: "Janakpur Temple View House",
        location: "Ramanand Chowk, Janakpur",
        lat: 26.7280, lng: 85.9250,
        price: 3400000, priceFormatted: "Rs 34,00,000",
        area: "1,350 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Munal Tamang", rating: 4.8, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 29,
        title: "Nepalgunj 6 Aana Stadium Plot",
        location: "Dhamboji, Nepalgunj",
        lat: 28.0520, lng: 81.6180,
        price: 2900000, priceFormatted: "Rs 29,00,000",
        area: "6 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Housing", rating: 4.6, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 30,
        title: "Dhangadhi Main Road House",
        location: "Chauraha, Dhangadhi",
        lat: 28.6920, lng: 80.5880,
        price: 3700000, priceFormatted: "Rs 37,00,000",
        area: "1,450 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Group", rating: 4.7, image: "images/villa.jpg", status: "Available", installmentAvailable: true
    },

    // 31-40: Eastern Nepal (Dharan, Itahari, Biratnagar, Birtamod)
    {
        id: 31,
        title: "Dharan Vijayapur Hill Villa",
        location: "Vijayapur, Dharan",
        lat: 26.8120, lng: 87.2830,
        price: 5200000, priceFormatted: "Rs 52,00,000",
        area: "1,700 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Together Housing", rating: 4.9, image: "images/villa.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 32,
        title: "Itahari 5 Aana Highway Ghaderi",
        location: "Highway Chowk, Itahari",
        lat: 26.6630, lng: 87.2750,
        price: 3500000, priceFormatted: "Rs 35,00,000",
        area: "5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Munal Tamang", rating: 4.8, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 33,
        title: "Biratnagar Main Road Apartment",
        location: "Main Road, Biratnagar",
        lat: 26.4550, lng: 87.2720,
        price: 4100000, priceFormatted: "Rs 41,00,000",
        area: "1,200 sq ft", type: "apartment", bedrooms: 2, bathrooms: 2,
        seller: "Rajesh Shrestha", rating: 4.7, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 34,
        title: "Birtamod 8 Aana Ghaderi",
        location: "Muktichowk, Birtamod",
        lat: 26.6350, lng: 87.9920,
        price: 4900000, priceFormatted: "Rs 49,00,000",
        area: "8 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.9, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 35,
        title: "Ilam Kanyam Tea Garden Cottage",
        location: "Kanyam, Ilam",
        lat: 26.9120, lng: 87.9250,
        price: 4600000, priceFormatted: "Rs 46,00,000",
        area: "1,500 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Housing", rating: 5.0, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 36,
        title: "Damak 5 Aana Colony Plot",
        location: "Damak, Jhapa",
        lat: 26.6680, lng: 87.6880,
        price: 2700000, priceFormatted: "Rs 27,00,000",
        area: "5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Sita Sharma", rating: 4.6, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 37,
        title: "Lahan 6 Aana Highway Plot",
        location: "Lahan, Siraha",
        lat: 26.7150, lng: 86.4830,
        price: 2200000, priceFormatted: "Rs 22,00,000",
        area: "6 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.5, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 38,
        title: "Dhankuta Hillside Residence",
        location: "Bazaar, Dhankuta",
        lat: 26.9820, lng: 87.3350,
        price: 3100000, priceFormatted: "Rs 31,00,000",
        area: "1,200 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Munal Tamang", rating: 4.8, image: "images/villa.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 39,
        title: "Chandragiri 4.5 Aana Hill Land",
        location: "Chandragiri, Kathmandu",
        lat: 27.6750, lng: 85.2250,
        price: 3900000, priceFormatted: "Rs 39,00,000",
        area: "4.5 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Housing", rating: 4.9, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 40,
        title: "Tokha Heritage House",
        location: "Tokha, Kathmandu",
        lat: 27.7580, lng: 85.3280,
        price: 5800000, priceFormatted: "Rs 58,00,000",
        area: "1,600 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Together Group", rating: 4.8, image: "images/community.jpg", status: "Available", installmentAvailable: true
    },

    // 41-52: More Nepalese Houses, Apartments & Land Plots
    {
        id: 41,
        title: "Imadol 4 Aana Ghaderi House",
        location: "Imadol, Lalitpur",
        lat: 27.6620, lng: 85.3420,
        price: 4900000, priceFormatted: "Rs 49,00,000",
        area: "1,400 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Housing", rating: 4.8, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 42,
        title: "Nakkhu Riverfront Apartment",
        location: "Nakkhu, Lalitpur",
        lat: 27.6580, lng: 85.3080,
        price: 4300000, priceFormatted: "Rs 43,00,000",
        area: "1,150 sq ft", type: "apartment", bedrooms: 2, bathrooms: 2,
        seller: "Munal Tamang", rating: 4.7, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 43,
        title: "Bhatbhateni Luxury Residence",
        location: "Bhatbhateni, Kathmandu",
        lat: 27.7180, lng: 85.3280,
        price: 7800000, priceFormatted: "Rs 78,00,000",
        area: "1,750 sq ft", type: "apartment", bedrooms: 3, bathrooms: 3,
        seller: "Together Group", rating: 5.0, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 44,
        title: "Patan Heritage Courtyard House",
        location: "Patan Durbar, Lalitpur",
        lat: 27.6730, lng: 85.3250,
        price: 6400000, priceFormatted: "Rs 64,00,000",
        area: "1,500 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Rajesh Shrestha", rating: 4.9, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 45,
        title: "Nagarkot 8 Aana Sunrise Plot",
        location: "Nagarkot, Bhaktapur",
        lat: 27.7150, lng: 85.5210,
        price: 4100000, priceFormatted: "Rs 41,00,000",
        area: "8 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Housing", rating: 4.9, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 46,
        title: "Kapan Monastery View House",
        location: "Kapan, Kathmandu",
        lat: 27.7380, lng: 85.3650,
        price: 5100000, priceFormatted: "Rs 51,00,000",
        area: "1,450 sq ft", type: "house", bedrooms: 3, bathrooms: 3,
        seller: "Sita Sharma", rating: 4.7, image: "images/community.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 47,
        title: "Dhumbarahi Executive Apartment",
        location: "Dhumbarahi, Kathmandu",
        lat: 27.7280, lng: 85.3450,
        price: 5900000, priceFormatted: "Rs 59,00,000",
        area: "1,350 sq ft", type: "apartment", bedrooms: 3, bathrooms: 2,
        seller: "Munal Tamang", rating: 4.8, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 48,
        title: "Balkhu Highway 6 Aana Plot",
        location: "Balkhu, Kathmandu",
        lat: 27.6850, lng: 85.2950,
        price: 6500000, priceFormatted: "Rs 65,00,000",
        area: "6 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Together Group", rating: 4.8, image: "images/Land_20191210081658.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 49,
        title: "Sitapaila Green Valley House",
        location: "Sitapaila, Kathmandu",
        lat: 27.7080, lng: 85.2750,
        price: 4700000, priceFormatted: "Rs 47,00,000",
        area: "1,300 sq ft", type: "house", bedrooms: 3, bathrooms: 2,
        seller: "Together Housing", rating: 4.7, image: "images/home.png", status: "Available", installmentAvailable: true
    },
    {
        id: 50,
        title: "Chobhar Gorge 4 Aana Ghaderi",
        location: "Chobhar, Kathmandu",
        lat: 27.6520, lng: 85.2920,
        price: 2950000, priceFormatted: "Rs 29,50,000",
        area: "4 Aana", type: "land", bedrooms: 0, bathrooms: 0,
        seller: "Rajesh Shrestha", rating: 4.6, image: "images/mountain_land.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 51,
        title: "Gattaghar Prime Colony House",
        location: "Gattaghar, Bhaktapur",
        lat: 27.6720, lng: 85.3720,
        price: 5300000, priceFormatted: "Rs 53,00,000",
        area: "1,550 sq ft", type: "house", bedrooms: 4, bathrooms: 3,
        seller: "Together Housing", rating: 4.9, image: "images/community.jpg", status: "Available", installmentAvailable: true
    },
    {
        id: 52,
        title: "Thamel Heritage Penthouse",
        location: "Thamel, Kathmandu",
        lat: 27.7150, lng: 85.3120,
        price: 9200000, priceFormatted: "Rs 92,00,000",
        area: "2,100 sq ft", type: "apartment", bedrooms: 4, bathrooms: 3,
        seller: "Munal Tamang", rating: 5.0, image: "images/apartment.jpg", status: "Available", installmentAvailable: true
    }
];

let leafletMap = null;
let markersGroup = null;

function renderProperties(props) {
    const container = document.getElementById('property-grid');
    const countEl = document.getElementById('prop-count');

    if (!container) return;

    if (countEl) countEl.textContent = props.length;

    if (props.length === 0) {
        container.innerHTML = `
      <div class="empty-state" style="grid-column: 1 / -1;">
        <i class="fas fa-search"></i>
        <h3>No properties match your filter</h3>
        <p>Try adjusting your search criteria or resetting filters.</p>
        <button class="btn btn-outline" onclick="resetFilters()">Reset Filters</button>
      </div>
    `;
        return;
    }

    container.innerHTML = props.map(p => `
    <article class="property-card">
      <div class="property-card-img-wrapper">
        <img src="${p.image}" alt="${p.title}" loading="lazy" />
        <span class="badge badge-accent property-badge-tag">${p.status}</span>
        <div class="property-price-tag">${p.priceFormatted}</div>
      </div>
      <div class="property-card-body">
        <div>
          <h3 class="property-title">${p.title}</h3>
          <p class="property-location"><i class="fas fa-map-marker-alt"></i> ${p.location}</p>
          <div class="property-specs">
            <span><i class="fas fa-ruler-combined"></i> ${p.area}</span>
            ${p.bedrooms ? `<span><i class="fas fa-bed"></i> ${p.bedrooms} Bed</span>` : ''}
            ${p.bathrooms ? `<span><i class="fas fa-bath"></i> ${p.bathrooms} Bath</span>` : ''}
          </div>
        </div>
        <div>
          <div class="property-seller-info">
            <span><i class="fas fa-store" style="color:var(--accent);"></i> ${p.seller}</span>
            <span class="stars"><i class="fas fa-star"></i> ${p.rating}</span>
          </div>
          <div class="flex gap-sm" style="margin-top: 12px;">
           <a href="/property-detail?id=${p.id}&seller=${encodeURIComponent(p.seller)}"
   class="btn btn-outline btn-sm"
   style="flex:1;">
   View Details
</a>
            <a href="booking.html?id=${p.id}" class="btn btn-primary btn-sm" style="flex:1;">Book Now</a>
          </div>
        </div>
      </div>
    </article>
  `).join('');

    updateMapMarkers(props);
}

function initMap() {
    const mapContainer = document.getElementById('properties-map');
    if (!mapContainer || typeof L === 'undefined') return;

    leafletMap = L.map('properties-map').setView([27.68, 85.35], 10);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(leafletMap);

    markersGroup = L.layerGroup().addTo(leafletMap);
    updateMapMarkers(sampleProperties);
}

function updateMapMarkers(props) {
    if (!leafletMap || !markersGroup) return;

    markersGroup.clearLayers();

    props.forEach(p => {
        const marker = L.marker([p.lat, p.lng]).addTo(markersGroup);
        marker.bindPopup(`
      <div style="width:180px; text-align:center;">
        <img src="${p.image}" style="width:100%; height:90px; object-fit:cover; border-radius:6px; margin-bottom:6px;" />
        <strong style="font-size:0.85rem; display:block;">${p.title}</strong>
        <div style="color:var(--accent); font-weight:700; font-size:0.9rem;">${p.priceFormatted}</div>
        <a href="property-detail.html?id=${p.id}" style="display:inline-block; margin-top:6px; padding:4px 10px; background:#003153; color:#fff; border-radius:4px; font-size:0.75rem; text-decoration:none;">View Details</a>
      </div>
    `);
    });
}

function toggleView(viewType) {
    const gridContainer = document.getElementById('property-grid');
    const mapContainer = document.getElementById('map-view-wrapper');
    const gridBtn = document.getElementById('btn-grid-view');
    const mapBtn = document.getElementById('btn-map-view');

    if (viewType === 'map') {
        gridContainer.style.display = 'none';
        mapContainer.style.display = 'block';
        gridBtn.classList.remove('btn-primary');
        gridBtn.classList.add('btn-ghost');
        mapBtn.classList.remove('btn-ghost');
        mapBtn.classList.add('btn-primary');

        if (!leafletMap) initMap();
        setTimeout(() => { if (leafletMap) leafletMap.invalidateSize(); }, 200);
    } else {
        gridContainer.style.display = 'grid';
        mapContainer.style.display = 'none';
        mapBtn.classList.remove('btn-primary');
        mapBtn.classList.add('btn-ghost');
        gridBtn.classList.remove('btn-ghost');
        gridBtn.classList.add('btn-primary');
    }
}

function filterProperties() {
    const query = document.getElementById('search-input')?.value.toLowerCase() || '';
    const type = document.getElementById('type-select')?.value || 'all';
    const maxPrice = parseInt(document.getElementById('price-range')?.value) || 10000000;

    const filtered = sampleProperties.filter(p => {
        const matchesSearch = p.title.toLowerCase().includes(query) || p.location.toLowerCase().includes(query);
        const matchesType = type === 'all' || p.type === type;
        const matchesPrice = p.price <= maxPrice;
        return matchesSearch && matchesType && matchesPrice;
    });

    renderProperties(filtered);
}

function resetFilters() {
    if (document.getElementById('search-input')) document.getElementById('search-input').value = '';
    if (document.getElementById('type-select')) document.getElementById('type-select').value = 'all';
    if (document.getElementById('price-range')) document.getElementById('price-range').value = 10000000;
    if (document.getElementById('price-display')) document.getElementById('price-display').textContent = 'Rs 1,00,00,000';
    renderProperties(sampleProperties);
}

document.addEventListener('DOMContentLoaded', () => {
    renderProperties(sampleProperties);

    const priceRange = document.getElementById('price-range');
    const priceDisplay = document.getElementById('price-display');

    if (priceRange && priceDisplay) {
        priceRange.addEventListener('input', (e) => {
            const val = parseInt(e.target.value);
            priceDisplay.textContent = `Rs ${(val / 100000).toFixed(0)} Lakhs`;
            filterProperties();
        });
    }
});
