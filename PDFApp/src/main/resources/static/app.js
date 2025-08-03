document.addEventListener('DOMContentLoaded', function() {
    // Set PDF.js worker
    pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.worker.min.js';

    // DOM elements
    const form = document.getElementById('pdfSignForm');
    const pdfFileInput = document.getElementById('pdfFile');
    const signatureFileInput = document.getElementById('signatureFile');
    const pdfFileInfo = document.getElementById('pdfFileInfo');
    const signatureFileInfo = document.getElementById('signatureFileInfo');
    
    // Signature drawing elements
    const uploadTab = document.getElementById('uploadTab');
    const drawTab = document.getElementById('drawTab');
    const uploadSignature = document.getElementById('uploadSignature');
    const drawSignature = document.getElementById('drawSignature');
    const signatureCanvas = document.getElementById('signatureCanvas');
    const clearSignature = document.getElementById('clearSignature');
    const saveSignature = document.getElementById('saveSignature');
    const signatureStatus = document.getElementById('signatureStatus');
    const signButton = document.getElementById('signButton');
    const loading = document.getElementById('loading');
    const result = document.getElementById('result');
    const success = document.getElementById('success');
    const error = document.getElementById('error');
    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');
    const downloadLink = document.getElementById('downloadLink');
    
    // PDF preview elements
    const pdfPreviewSection = document.getElementById('pdfPreviewSection');
    const pdfCanvas = document.getElementById('pdfCanvas');
    const signaturePreview = document.getElementById('signaturePreview');
    const pageNumberInput = document.getElementById('pageNumber');
    const widthInput = document.getElementById('width');
    const heightInput = document.getElementById('height');
    const xInput = document.getElementById('x');
    const yInput = document.getElementById('y');
    const selectedX = document.getElementById('selectedX');
    const selectedY = document.getElementById('selectedY');
    const clearSelection = document.getElementById('clearSelection');
    const prevPage = document.getElementById('prevPage');
    const nextPage = document.getElementById('nextPage');
    const pageInfo = document.getElementById('pageInfo');

    // PDF state
    let pdfDoc = null;
    let currentPage = 1;
    let scale = 1.5;
    let selectedPosition = null;
    
    // Signature drawing state
    let isDrawing = false;
    let hasDrawnSignature = false;
    let signatureCtx = signatureCanvas.getContext('2d');
    let currentSignatureMethod = 'upload'; // 'upload' or 'draw'
    
    // Initialize signature canvas
    signatureCtx.strokeStyle = '#000';
    signatureCtx.lineWidth = 2;
    signatureCtx.lineCap = 'round';

    // Event listeners
    pdfFileInput.addEventListener('change', handlePDFUpload);
    signatureFileInput.addEventListener('change', handleSignatureUpload);
    pdfCanvas.addEventListener('click', handleCanvasClick);
    clearSelection.addEventListener('click', clearSignatureSelection);
    prevPage.addEventListener('click', () => navigatePage(-1));
    nextPage.addEventListener('click', () => navigatePage(1));
    pageNumberInput.addEventListener('change', handlePageNumberChange);
    widthInput.addEventListener('input', updateSignaturePreview);
    heightInput.addEventListener('input', updateSignaturePreview);
    form.addEventListener('submit', submitForm);
    
    // Signature drawing event listeners
    uploadTab.addEventListener('click', () => switchSignatureMethod('upload'));
    drawTab.addEventListener('click', () => switchSignatureMethod('draw'));
    clearSignature.addEventListener('click', clearSignatureCanvas);
    saveSignature.addEventListener('click', saveSignatureImage);
    
    // Canvas drawing events
    signatureCanvas.addEventListener('mousedown', startDrawing);
    signatureCanvas.addEventListener('mousemove', draw);
    signatureCanvas.addEventListener('mouseup', stopDrawing);
    signatureCanvas.addEventListener('mouseout', stopDrawing);
    
    // Touch events for mobile
    signatureCanvas.addEventListener('touchstart', handleTouchStart);
    signatureCanvas.addEventListener('touchmove', handleTouchMove);
    signatureCanvas.addEventListener('touchend', stopDrawing);

    function handlePDFUpload(e) {
        const file = e.target.files[0];
        if (file) {
            pdfFileInfo.textContent = `Selected: ${file.name} (${formatFileSize(file.size)})`;
            loadPDFPreview(file);
            validateForm();
        } else {
            pdfFileInfo.textContent = '';
            pdfPreviewSection.style.display = 'none';
        }
    }

    function handleSignatureUpload(e) {
        const file = e.target.files[0];
        if (file) {
            signatureFileInfo.textContent = `Selected: ${file.name} (${formatFileSize(file.size)})`;
            // Switch to upload method when file is selected
            currentSignatureMethod = 'upload';
            validateForm();
        } else {
            signatureFileInfo.textContent = '';
            validateForm();
        }
    }

    async function loadPDFPreview(file) {
        try {
            const arrayBuffer = await file.arrayBuffer();
            pdfDoc = await pdfjsLib.getDocument(arrayBuffer).promise;
            
            pageInfo.textContent = `Page ${currentPage} of ${pdfDoc.numPages}`;
            updatePageControls();
            
            await renderPage(currentPage);
            pdfPreviewSection.style.display = 'block';
            
        } catch (error) {
            console.error('Error loading PDF:', error);
            showError('Failed to load PDF preview');
        }
    }

    async function renderPage(pageNum) {
        try {
            const page = await pdfDoc.getPage(pageNum);
            const viewport = page.getViewport({ scale: scale });
            
            pdfCanvas.height = viewport.height;
            pdfCanvas.width = viewport.width;
            
            const ctx = pdfCanvas.getContext('2d');
            const renderContext = {
                canvasContext: ctx,
                viewport: viewport
            };
            
            await page.render(renderContext).promise;
            
            // Update page number input
            pageNumberInput.value = pageNum;
            pageNumberInput.max = pdfDoc.numPages;
            
            // Clear previous selection when changing pages
            clearSignatureSelection();
            
        } catch (error) {
            console.error('Error rendering page:', error);
            showError('Failed to render PDF page');
        }
    }

    function handleCanvasClick(e) {
        const rect = pdfCanvas.getBoundingClientRect();
        const x = (e.clientX - rect.left) * (pdfCanvas.width / rect.width);
        const y = (e.clientY - rect.top) * (pdfCanvas.height / rect.height);
        
        // Convert to PDF coordinate system (bottom-left origin)
        const pdfY = pdfCanvas.height - y;
        
        selectedPosition = { x: Math.round(x), y: Math.round(pdfY) };
        
        // Update hidden inputs
        xInput.value = selectedPosition.x;
        yInput.value = selectedPosition.y;
        
        // Update display
        selectedX.textContent = selectedPosition.x;
        selectedY.textContent = selectedPosition.y;
        
        updateSignaturePreview();
        validateForm();
    }

    function updateSignaturePreview() {
        if (!selectedPosition) {
            signaturePreview.style.display = 'none';
            return;
        }
        
        const rect = pdfCanvas.getBoundingClientRect();
        const canvasRect = pdfCanvas.getBoundingClientRect();
        
        // Calculate position relative to canvas display
        const displayX = (selectedPosition.x / pdfCanvas.width) * canvasRect.width;
        const displayY = ((pdfCanvas.height - selectedPosition.y) / pdfCanvas.height) * canvasRect.height;
        
        // Calculate size relative to scale
        const displayWidth = (parseInt(widthInput.value) / pdfCanvas.width) * canvasRect.width;
        const displayHeight = (parseInt(heightInput.value) / pdfCanvas.height) * canvasRect.height;
        
        signaturePreview.style.display = 'block';
        signaturePreview.style.left = `${displayX}px`;
        signaturePreview.style.top = `${displayY}px`;
        signaturePreview.style.width = `${displayWidth}px`;
        signaturePreview.style.height = `${displayHeight}px`;
    }

    function clearSignatureSelection() {
        selectedPosition = null;
        selectedX.textContent = '-';
        selectedY.textContent = '-';
        signaturePreview.style.display = 'none';
        xInput.value = '100';
        yInput.value = '200';
        validateForm();
    }

    function navigatePage(direction) {
        const newPage = currentPage + direction;
        if (newPage >= 1 && newPage <= pdfDoc.numPages) {
            currentPage = newPage;
            renderPage(currentPage);
            pageInfo.textContent = `Page ${currentPage} of ${pdfDoc.numPages}`;
            updatePageControls();
        }
    }

    function handlePageNumberChange() {
        const pageNum = parseInt(pageNumberInput.value);
        if (pageNum >= 1 && pageNum <= pdfDoc.numPages && pageNum !== currentPage) {
            currentPage = pageNum;
            renderPage(currentPage);
            pageInfo.textContent = `Page ${currentPage} of ${pdfDoc.numPages}`;
            updatePageControls();
        }
    }

    function updatePageControls() {
        prevPage.disabled = currentPage <= 1;
        nextPage.disabled = currentPage >= pdfDoc.numPages;
    }

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
        const hasSignature = (currentSignatureMethod === 'upload' && signatureFile) || 
                           (currentSignatureMethod === 'draw' && hasDrawnSignature);
        
        // Debug validation (remove in production)
        if (window.location.hostname === 'localhost') {
            console.log('Validation:', {
                pdfFile: !!pdfFile,
                currentSignatureMethod,
                signatureFile: !!signatureFile,
                hasDrawnSignature,
                hasSignature,
                selectedPosition: !!selectedPosition
            });
        }
        
        if (pdfFile && hasSignature && selectedPosition) {
            if (!isValidPDF(pdfFile)) {
                showError('Please select a valid PDF file.');
                return false;
            }
            
            if (currentSignatureMethod === 'upload' && signatureFile && !isValidImage(signatureFile)) {
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

    function submitForm(e) {
        e.preventDefault();
        
        if (!validateForm()) {
            if (window.location.hostname === 'localhost') {
                console.log('Form validation failed');
            }
            return;
        }

        const formData = new FormData();
        
        // Always add the PDF file
        formData.append('pdfFile', pdfFileInput.files[0]);
        formData.append('pageNumber', pageNumberInput.value);
        formData.append('x', xInput.value);
        formData.append('y', yInput.value);
        formData.append('width', widthInput.value);
        formData.append('height', heightInput.value);
        
        // Add signature data based on method
        if (currentSignatureMethod === 'draw' && hasDrawnSignature) {
            const signatureDataUrl = signatureCanvas.toDataURL('image/png');
            formData.append('signatureData', signatureDataUrl);
            if (window.location.hostname === 'localhost') {
                console.log('Using drawn signature');
            }
        } else if (currentSignatureMethod === 'upload' && signatureFileInput.files[0]) {
            formData.append('signatureFile', signatureFileInput.files[0]);
            if (window.location.hostname === 'localhost') {
                console.log('Using uploaded signature file');
            }
        }
        
        if (window.location.hostname === 'localhost') {
            console.log('Submitting form with method:', currentSignatureMethod);
        }
        
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
            if (window.location.hostname === 'localhost') {
                console.log('Response:', data);
            }
            
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

    // Signature drawing functions
    function switchSignatureMethod(method) {
        if (window.location.hostname === 'localhost') {
            console.log('Switching to signature method:', method);
        }
        currentSignatureMethod = method;
        
        if (method === 'upload') {
            uploadTab.classList.add('active');
            drawTab.classList.remove('active');
            uploadSignature.style.display = 'block';
            drawSignature.style.display = 'none';
        } else {
            drawTab.classList.add('active');
            uploadTab.classList.remove('active');
            uploadSignature.style.display = 'none';
            drawSignature.style.display = 'block';
        }
        
        validateForm();
    }
    
    function startDrawing(e) {
        isDrawing = true;
        const rect = signatureCanvas.getBoundingClientRect();
        signatureCtx.beginPath();
        signatureCtx.moveTo(e.clientX - rect.left, e.clientY - rect.top);
        if (window.location.hostname === 'localhost') {
            console.log('Started drawing');
        }
    }
    
    function draw(e) {
        if (!isDrawing) return;
        
        const rect = signatureCanvas.getBoundingClientRect();
        signatureCtx.lineTo(e.clientX - rect.left, e.clientY - rect.top);
        signatureCtx.stroke();
        
        if (!hasDrawnSignature) {
            hasDrawnSignature = true;
            signatureStatus.textContent = 'Signature drawn';
            if (window.location.hostname === 'localhost') {
                console.log('Signature drawn, validating form');
            }
            validateForm();
        }
    }
    
    function stopDrawing() {
        isDrawing = false;
        signatureCtx.beginPath();
    }
    
    function handleTouchStart(e) {
        e.preventDefault();
        const touch = e.touches[0];
        const rect = signatureCanvas.getBoundingClientRect();
        isDrawing = true;
        signatureCtx.beginPath();
        signatureCtx.moveTo(touch.clientX - rect.left, touch.clientY - rect.top);
    }
    
    function handleTouchMove(e) {
        e.preventDefault();
        if (!isDrawing) return;
        
        const touch = e.touches[0];
        const rect = signatureCanvas.getBoundingClientRect();
        signatureCtx.lineTo(touch.clientX - rect.left, touch.clientY - rect.top);
        signatureCtx.stroke();
        
        hasDrawnSignature = true;
        signatureStatus.textContent = 'Signature drawn';
        validateForm();
    }
    
    function clearSignatureCanvas() {
        signatureCtx.clearRect(0, 0, signatureCanvas.width, signatureCanvas.height);
        hasDrawnSignature = false;
        signatureStatus.textContent = '';
        validateForm();
    }
    
    function saveSignatureImage() {
        if (!hasDrawnSignature) {
            showError('Please draw a signature first');
            return;
        }
        
        // Create download link
        const dataUrl = signatureCanvas.toDataURL('image/png');
        const link = document.createElement('a');
        link.download = 'signature.png';
        link.href = dataUrl;
        link.click();
        
        signatureStatus.textContent = 'Signature saved as PNG';
    }

    // Initialize form validation
    validateForm();
});