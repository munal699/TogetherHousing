
// Booking Calculator Logic
function calculateInstallment() {
    const priceEl = document.getElementById('property-price');
    const price = priceEl ? (parseFloat(priceEl.value) || 3000000) : 3000000;
    const downPercent = parseInt(document.getElementById('down-percent')?.value || '20');
    const durationMonths = parseInt(document.getElementById('duration-select')?.value || '24');

    const downPayment = (price * downPercent) / 100;
    const remaining = Math.max(0, price - downPayment);
    const monthly = durationMonths > 0 ? (remaining / durationMonths) : 0;

    const actualDownEl = document.getElementById('actual-down-payment');
    if (actualDownEl) {
        actualDownEl.value = downPayment;
    }

    if (document.getElementById('calc-down')) document.getElementById('calc-down').textContent = `Rs ${Math.round(downPayment).toLocaleString()}`;
    if (document.getElementById('calc-rem')) document.getElementById('calc-rem').textContent = `Rs ${Math.round(remaining).toLocaleString()}`;
    if (document.getElementById('calc-monthly')) document.getElementById('calc-monthly').textContent = `Rs ${Math.round(monthly).toLocaleString()} / mo`;
}

document.addEventListener('DOMContentLoaded', () => {
    calculateInstallment();
});
