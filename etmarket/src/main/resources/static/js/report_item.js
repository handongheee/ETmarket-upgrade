document.addEventListener('DOMContentLoaded', function () {
    const dropdowns = document.querySelectorAll('.dropdown');

    dropdowns.forEach(dropdown => {

        const select = dropdown.querySelector('.select');
        const caret = dropdown.querySelector('.caret');
        const menu = dropdown.querySelector('.menu');
        const options = dropdown.querySelectorAll('.menu li');
        const selected = dropdown.querySelector('.selected');
        const reasonInput = document.getElementById('reason');

        select.addEventListener('click', () => {
            select.classList.toggle('select-clicked');
            caret.classList.toggle('caret-rotate');
            menu.classList.toggle('menu-open');
        });

        options.forEach(option => {
            option.addEventListener('click', () => {
                selected.innerText = option.innerText;
                reasonInput.value = option.getAttribute('value');
                select.classList.remove('select-clicked');
                caret.classList.remove('caret-rotate');
                menu.classList.remove('menu-open');
                options.forEach(option => {
                    option.classList.remove('active');
                });

                option.classList.add('active');
            });
        });
    });
    document.getElementById('reportForm').addEventListener('submit', function(event) {
        const reasonInput = document.getElementById('reason');

        if (!reasonInput.value) {
            alert('신고 사유를 선택해주세요.');
            event.preventDefault();
            return;
        }

        event.preventDefault();

        var form = event.target;
        var formData = new FormData(form);

        fetch(form.action, {
            method: form.method,
            body: formData,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('신고접수가 완료되었습니다');
                    window.location.href = '/';
                } else {
                    alert('오류가 발생했습니다. 다시 시도해주세요.');
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다. 다시 시도해주세요.');
                console.error('Error:', error);
            });
    });
});