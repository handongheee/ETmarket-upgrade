document.addEventListener('DOMContentLoaded', function() {
    /*카테고리 작동*/
    var menuIcon = document.querySelector('.bi-list');
    var verticalMenu = document.querySelector('.vertical-menu');

    if (menuIcon && verticalMenu) {
        menuIcon.addEventListener('mouseenter', function() {
            verticalMenu.style.display = 'block';

            if (getComputedStyle(verticalMenu).display === 'block') {
                menuIcon.style.color = '#4eb006';
            }
        });

        menuIcon.addEventListener('mouseleave', function() {
            setTimeout(function() {
                if (!verticalMenu.matches(':hover') && !menuIcon.matches(':hover')) {
                    verticalMenu.style.display = 'none';

                    if (getComputedStyle(verticalMenu).display === 'none') {
                        menuIcon.style.color = '';
                    }
                }
            }, 500);

        });

        verticalMenu.addEventListener('mouseenter', function() {
            verticalMenu.style.display = 'block';

            if (getComputedStyle(verticalMenu).display === 'block') {
                menuIcon.style.color = '#4eb006';
            }
        });

        verticalMenu.addEventListener('mouseleave', function() {
            setTimeout(function() {
                if (!verticalMenu.matches(':hover') && !menuIcon.matches(':hover')) {
                    verticalMenu.style.display = 'none';

                    if (getComputedStyle(verticalMenu).display === 'none') {
                        menuIcon.style.color = '';
                    }
                }
            }, 500);
        });

    }

    /*검색창 검색기록 작동*/
    var inputField = document.querySelector(".cLfdog");
    var searchBox = document.querySelector(".eLTnVY");
    var closeBtn = document.querySelector(".iotYfC");

    inputField.addEventListener("click", function(event) {
        searchBox.style.display = "block";
        event.stopPropagation();
    });

    searchBox.addEventListener("click", function(event) {
        event.stopPropagation();
    });

    document.addEventListener("click", function() {
        searchBox.style.display = "none";
    });

    closeBtn.addEventListener("click", function() {
        searchBox.style.display = "none";
    });

    // 채팅

});
