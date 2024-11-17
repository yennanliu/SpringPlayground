// 自己组装
let uploadButton = document.querySelector('.uploadButton');
uploadButton.addEventListener('click', () => {
	let form = new FormData();
	form.append('name', document.querySelector('.formName').value);
	form.append('file', document.querySelector('.uploadFile').files[0]);
	fetch('/upload', {
		method: 'POST',
		body: form
	}).then(response => response.json()).then((result) => {
		console.log(result);
	});
});