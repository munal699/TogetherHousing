
// Booking Calculator Logic
function calculateInstallment() {
    const price = 3000000;
    const downPercent = parseInt(document.getElementById('down-percent')?.value || '20');
    const durationMonths = parseInt(document.getElementById('duration-select')?.value || '24');

    const downPayment = (price * downPercent) / 100;
    const remaining = price - downPayment;
    const monthly = remaining / durationMonths;

    if (document.getElementById('calc-down')) document.getElementById('calc-down').textContent = `Rs ${downPayment.toLocaleString()}`;
    if (document.getElementById('calc-rem')) document.getElementById('calc-rem').textContent = `Rs ${remaining.toLocaleString()}`;
    if (document.getElementById('calc-monthly')) document.getElementById('calc-monthly').textContent = `Rs ${Math.round(monthly).toLocaleString()} / mo`;
}

function handleBookingSubmit(e) {
    e.preventDefault();
    showToast('Processing your property booking...', 'success');
    setTimeout(() => {
        window.location.href = 'booking-confirmation.html';
    }, 1200);
}

document.addEventListener('DOMContentLoaded', () => {
    calculateInstallment();
});
