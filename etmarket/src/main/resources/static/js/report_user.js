document.addEventListener('DOMContentLoaded', function () {

    const reportContent = document.getElementById('reportContent');
    document.getElementById('reportForm').addEventListener('submit', function(event) {

        event.preventDefault();
        if (reportContent.textLength < 10) {
            console.log(reportContent.textLength)
            alert('10자 이상 작성해주세요.');
            return;
        }

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