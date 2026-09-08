/* ═══════════════════════════════════════════════════════════════
   Together Group — Main JavaScript (Shared Utilities + Dark Mode + Modals)
   ═══════════════════════════════════════════════════════════════ */

// ── Set current year in footer ──
document.addEventListener('DOMContentLoaded', () => {
    const yearEls = document.querySelectorAll('.js-year');
    yearEls.forEach(el => el.textContent = new Date().getFullYear());
});

// ── Theme Switcher (Dark / Light Mode) ──
function initThemeToggle() {
    const savedTheme = localStorage.getItem('tg_theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
    updateThemeIcons(savedTheme);

    document.querySelectorAll('.js-theme-toggle').forEach(btn => {
        btn.addEventListener('click', () => {
            const currentTheme = document.documentElement.getAttribute('data-theme') || 'light';
            const newTheme = currentTheme === 'light' ? 'dark' : 'light';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('tg_theme', newTheme);
            updateThemeIcons(newTheme);
            showToast(`Switched to ${newTheme === 'dark' ? 'Dark' : 'Light'} Mode`, 'info');
        });
    });
}

function updateThemeIcons(theme) {
    document.querySelectorAll('.js-theme-toggle i').forEach(icon => {
        icon.className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    });
}

// ── Mobile Menu Toggle ──
function initMobileMenu() {
    const hamburger = document.getElementById('tg-hamburger');
    const mobileMenu = document.getElementById('tg-mobile-menu');

    if (!hamburger || !mobileMenu) return;

    hamburger.addEventListener('click', () => {
        const expanded = hamburger.getAttribute('aria-expanded') === 'true';
        hamburger.setAttribute('aria-expanded', String(!expanded));
        mobileMenu.classList.toggle('open');
        mobileMenu.setAttribute('aria-hidden', String(expanded));
    });

    // Close on outside click
    document.addEventListener('click', (e) => {
        if (!mobileMenu.contains(e.target) && !hamburger.contains(e.target)) {
            mobileMenu.classList.remove('open');
            mobileMenu.setAttribute('aria-hidden', 'true');
            hamburger.setAttribute('aria-expanded', 'false');
        }
    });
}

// ── Dashboard Sidebar Toggle (Mobile) ──
function initDashSidebar() {
    const toggle = document.querySelector('.dash-sidebar-toggle');
    const sidebar = document.querySelector('.dash-sidebar');
    const overlay = document.querySelector('.dash-overlay');

    if (!toggle || !sidebar) return;

    toggle.addEventListener('click', () => {
        sidebar.classList.toggle('open');
        if (overlay) overlay.classList.toggle('active');
    });

    if (overlay) {
        overlay.addEventListener('click', () => {
            sidebar.classList.remove('open');
            overlay.classList.remove('active');
        });
    }
}

// ── Dashboard Tab Navigation ──
function initDashTabs() {
    const navItems = document.querySelectorAll('.dash-nav-item[data-section]');
    const sections = document.querySelectorAll('.dash-section');

    if (!navItems.length || !sections.length) return;

    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const target = item.getAttribute('data-section');

            navItems.forEach(n => n.classList.remove('active'));
            item.classList.add('active');

            sections.forEach(s => {
                s.classList.toggle('active', s.id === target);
            });

            const sidebar = document.querySelector('.dash-sidebar');
            const overlay = document.querySelector('.dash-overlay');
            if (sidebar) sidebar.classList.remove('open');
            if (overlay) overlay.classList.remove('active');
        });
    });
}

// ── Modal Utility ──
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = '';
    }
}

document.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('active');
        document.body.style.overflow = '';
    }
});

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        const activeModal = document.querySelector('.modal-overlay.active');
        if (activeModal) {
            activeModal.classList.remove('active');
            document.body.style.overflow = '';
        }
    }
});

// ── Month-by-Month Installment Schedule Modal Generator ──
function generateInstallmentScheduleModal(price = 3000000, downPercent = 20, months = 24, propertyTitle = "Bhaktapur Modern Villa") {
    const downPayment = (price * downPercent) / 100;
    const remaining = price - downPayment;
    const monthly = remaining / months;

    let modalHtml = `
    <div id="schedule-modal" class="modal-overlay active">
      <div class="modal-content" style="max-width: 720px;">
        <div class="modal-header">
          <div>
            <h3><i class="fas fa-calendar-alt" style="color:var(--accent);"></i> ${months}-Month Installment Breakdown</h3>
            <small class="text-muted">${propertyTitle} — Total Price: Rs ${price.toLocaleString()}</small>
          </div>
          <button class="modal-close" onclick="closeModal('schedule-modal')">&times;</button>
        </div>

        <div style="background:var(--bg); padding:16px; border-radius:var(--radius); margin-bottom:20px;">
          <div class="flex justify-between" style="font-size:0.9rem; margin-bottom:4px;">
            <span>Down Payment (${downPercent}%): <strong>Rs ${downPayment.toLocaleString()}</strong></span>
            <span>Financed Amount: <strong>Rs ${remaining.toLocaleString()}</strong></span>
          </div>
          <div class="flex justify-between" style="font-size:0.9rem;">
            <span>Monthly Payment: <strong style="color:var(--accent);">Rs ${Math.round(monthly).toLocaleString()} / mo</strong></span>
            <span>Duration: <strong>${months} Months</strong></span>
          </div>
        </div>

        <div class="table-wrapper" style="max-height: 340px; overflow-y: auto;">
          <table class="tg-table">
            <thead>
              <tr>
                <th>Month</th>
                <th>Due Date</th>
                <th>Payment Amount</th>
                <th>Remaining Balance</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
  `;

    let currentBalance = remaining;
    const startDate = new Date();

    for (let i = 1; i <= months; i++) {
        const dueDate = new Date(startDate.getFullYear(), startDate.getMonth() + i, 15);
        const dateStr = dueDate.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
        currentBalance = Math.max(0, currentBalance - monthly);

        const isPaid = i === 1; // Down payment paid scenario
        modalHtml += `
      <tr>
        <td><strong>Month ${i}</strong></td>
        <td>${dateStr}</td>
        <td>Rs ${Math.round(monthly).toLocaleString()}</td>
        <td>Rs ${Math.round(currentBalance).toLocaleString()}</td>
        <td>
          <span class="badge ${isPaid ? 'badge-success' : 'badge-primary'}">${isPaid ? 'Paid' : 'Pending'}</span>
        </td>
      </tr>
    `;
    }

    modalHtml += `
            </tbody>
          </table>
        </div>

        <div class="modal-footer" style="justify-content: space-between;">
          <button class="btn btn-ghost btn-sm" onclick="window.print()"><i class="fas fa-print"></i> Print Schedule</button>
          <button class="btn btn-primary btn-sm" onclick="closeModal('schedule-modal')">Close Breakdown</button>
        </div>
      </div>
    </div>
  `;

    // Remove existing modal if any
    const oldModal = document.getElementById('schedule-modal');
    if (oldModal) oldModal.remove();

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    document.body.style.overflow = 'hidden';
}

// ── Password Visibility Toggle ──
function initPasswordToggles() {
    document.querySelectorAll('.toggle-password').forEach(btn => {
        btn.addEventListener('click', () => {
            const input = btn.parentElement.querySelector('input');
            if (!input) return;
            const isPassword = input.type === 'password';
            input.type = isPassword ? 'text' : 'password';
            btn.querySelector('i').className = isPassword ? 'fas fa-eye-slash' : 'fas fa-eye';
        });
    });
}

// ── Role Tab Selector (Auth pages) ──
function initRoleTabs() {
    const roles = document.querySelectorAll('.auth-role');
    const roleInput = document.getElementById('role-input');

    if (!roles.length) return;

    roles.forEach(role => {
        role.addEventListener('click', () => {
            roles.forEach(r => r.classList.remove('active'));
            role.classList.add('active');
            if (roleInput) roleInput.value = role.dataset.role;
        });
    });
}

// ── Toast Notification ──
function showToast(message, type = 'success', duration = 3000) {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
    <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'times-circle' : 'info-circle'}"></i>
    <span>${message}</span>
  `;

    Object.assign(toast.style, {
        position: 'fixed',
        bottom: '24px',
        right: '24px',
        background: type === 'success' ? 'var(--success)' : type === 'error' ? 'var(--danger)' : 'var(--info)',
        color: '#fff',
        padding: '14px 20px',
        borderRadius: 'var(--radius)',
        boxShadow: 'var(--shadow-lg)',
        display: 'flex',
        alignItems: 'center',
        gap: '10px',
        fontWeight: '600',
        fontSize: '0.9rem',
        zIndex: '9999',
        animation: 'fadeInUp 0.3s ease',
        maxWidth: '360px'
    });

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'fadeIn 0.3s ease reverse';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ── Initialize Everything ──
document.addEventListener('DOMContentLoaded', () => {
    initThemeToggle();
    initMobileMenu();
    initDashSidebar();
    initDashTabs();
    initPasswordToggles();
    initRoleTabs();
});
