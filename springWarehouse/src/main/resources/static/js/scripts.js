// Add smooth scrolling
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Add fade-in animation to elements as they scroll into view
document.addEventListener('DOMContentLoaded', function() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
            }
        });
    });
    
    document.querySelectorAll('.custom-card, .container').forEach(el => {
        observer.observe(el);
    });
});

// Form validation enhancement
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('.modern-form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            } else {
                // Show a nice success message instead of submitting
                event.preventDefault();
                showSuccessMessage(form);
            }
            
            form.classList.add('was-validated');
        });
    });
    
    function showSuccessMessage(form) {
        const formContent = form.innerHTML;
        form.innerHTML = `
            <div class="text-center py-4">
                <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="currentColor" class="bi bi-check-circle-fill text-success mb-4" viewBox="0 0 16 16">
                    <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
                </svg>
                <h3>Thank You!</h3>
                <p>Your message has been sent successfully.</p>
                <button class="btn btn-outline-primary mt-3" id="resetForm">Send Another Message</button>
            </div>
        `;
        
        document.getElementById('resetForm').addEventListener('click', function() {
            form.innerHTML = formContent;
        });
    }
}); 