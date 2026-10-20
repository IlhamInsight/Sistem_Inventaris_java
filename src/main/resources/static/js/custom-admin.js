// custom-admin.js
document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');
    
    // Cek apakah elemen ada sebelum memasang event
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            // Kita bisa menggunakan class 'show-sidebar' atau toggle manual
            sidebar.classList.toggle('d-none'); // Toggle utility bootstrap
            sidebar.classList.toggle('d-block');
            
            // Atau jika menggunakan CSS custom yang kita buat tadi:
            // sidebar.classList.toggle('show-sidebar');
        });
    }
});

// Di dalam custom-admin.js
(function () {
  'use strict'
  var forms = document.querySelectorAll('.needs-validation')
  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener('submit', function (event) {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }
      form.classList.add('was-validated')
    }, false)
  })
})()

// custom-admin.js

(function () {
    'use strict'

    // Ambil semua form yang ingin kita beri validasi kustom Bootstrap
    var forms = document.querySelectorAll('.needs-validation')

    // Loop form tersebut dan cegah pengiriman jika tidak valid
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                
                // Validasi Tambahan Kustom (Opsional)
                const emailInput = form.querySelector('input[type="email"]');
                const phoneInput = form.querySelector('input[th\\:field*="phone"]'); // Menyesuaikan binding thymeleaf

                if (phoneInput) {
                const phoneRegex = /^(?:\+62|62|0)8[1-9][0-9]{7,10}$/;
                if (!phoneRegex.test(phoneInput.value)) {
                    phoneInput.setCustomValidity('Invalid');
                } else {
                    phoneInput.setCustomValidity('');
                }
              }

                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        })
})();