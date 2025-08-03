document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('pdfSignForm');
    const pdfFileInput = document.getElementById('pdfFile');
    const signatureFileInput = document.getElementById('signatureFile');
    const pdfFileInfo = document.getElementById('pdfFileInfo');
    const signatureFileInfo = document.getElementById('signatureFileInfo');
    const signButton = document.getElementById('signButton');
    const loading = document.getElementById('loading');
    const result = document.getElementById('result');
    const success = document.getElementById('success');
    const error = document.getElementById('error');
    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');
    const downloadLink = document.getElementById('downloadLink');

    pdfFileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            pdfFileInfo.textContent = `Selected: ${file.name} (${formatFileSize(file.size)})`;
            validateForm();
        } else {
            pdfFileInfo.textContent = '';
        }
    });

    signatureFileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            signatureFileInfo.textContent = `Selected: ${file.name} (${formatFileSize(file.size)})`;
            validateForm();
        } else {
            signatureFileInfo.textContent = '';
        }
    });

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm();
    });

    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    function validateForm() {
        const pdfFile = pdfFileInput.files[0];
        const signatureFile = signatureFileInput.files[0];
        
        if (pdfFile && signatureFile) {
            if (!isValidPDF(pdfFile)) {
                showError('Please select a valid PDF file.');
                return false;
            }
            
            if (!isValidImage(signatureFile)) {
                showError('Please select a valid image file (PNG or JPEG).');
                return false;
            }
            
            signButton.disabled = false;
            hideResult();
            return true;
        } else {
            signButton.disabled = true;
            return false;
        }
    }

    function isValidPDF(file) {
        return file.type === 'application/pdf' || file.name.toLowerCase().endsWith('.pdf');
    }

    function isValidImage(file) {
        const validTypes = ['image/png', 'image/jpeg', 'image/jpg'];
        return validTypes.includes(file.type) || 
               file.name.toLowerCase().match(/\.(png|jpg|jpeg)$/);
    }

    function submitForm() {
        if (!validateForm()) {
            return;
        }

        const formData = new FormData(form);
        
        signButton.disabled = true;
        showLoading();
        hideResult();

        fetch('/api/pdf/sign', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            hideLoading();
            signButton.disabled = false;
            
            if (data.success) {
                showSuccess(data.message, data.fileName);
            } else {
                showError(data.message);
            }
        })
        .catch(error => {
            hideLoading();
            signButton.disabled = false;
            console.error('Error:', error);
            showError('An unexpected error occurred. Please try again.');
        });
    }

    function showLoading() {
        loading.classList.remove('hidden');
    }

    function hideLoading() {
        loading.classList.add('hidden');
    }

    function showSuccess(message, fileName) {
        successMessage.textContent = message;
        downloadLink.href = `/api/pdf/download/${fileName}`;
        downloadLink.download = fileName;
        
        success.classList.remove('hidden');
        error.classList.add('hidden');
        result.classList.remove('hidden');
    }

    function showError(message) {
        errorMessage.textContent = message;
        
        error.classList.remove('hidden');
        success.classList.add('hidden');
        result.classList.remove('hidden');
    }

    function hideResult() {
        result.classList.add('hidden');
        success.classList.add('hidden');
        error.classList.add('hidden');
    }

    document.querySelectorAll('input[type="number"]').forEach(input => {
        input.addEventListener('input', function() {
            if (this.value < 0) {
                this.value = 0;
            }
        });
    });

    validateForm();
});