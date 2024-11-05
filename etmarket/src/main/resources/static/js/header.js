document.addEventListener('DOMContentLoaded', function() {
    const myStoreLink = document.getElementById('myStoreLink');
    myStoreLink.addEventListener('click', function(event) {

        $.ajax({
            url: '/check-login-status',
            type: 'GET',
            success: function(response) {
                if (!response.isLoggedin) {
                    alert("로그인이 필요합니다");
                    window.location.href = '/login?redirect=' + encodeURIComponent(window.location.href);
                } else {
                    // 로그인된 상태, 원하는 URL로 이동
                    window.location.href = '/seller/' + response.userId + '/items';
                }
            },
            error: function() {
                alert('로그인 상태 확인 중 오류가 발생했습니다.');
            }
        });
    });
});