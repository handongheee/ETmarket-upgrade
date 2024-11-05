document.addEventListener('DOMContentLoaded', function () {
    const images = document.querySelectorAll('.product-image-list img');
    const imageCountBox = document.querySelector('.image-count-box');
    const prevButton = document.querySelector('.prev-button');
    const nextButton = document.querySelector('.next-button');
    const zoomButton = document.querySelector('.zoom-button');
    const productImageWrapper = document.querySelector('.product-image-wrapper');
    const modal = document.getElementById('zoom-image-modal');
    const modalImage = modal.querySelector('.modal-image');
    const modalImageBox = modal.querySelector('.modal-image-box');
    const modalImageCountBox = modal.querySelector('.modal-footer-count-box');
    const exitModalButton = modal.querySelector('.exit-modal');
    const categoryBox = document.querySelector('.category-box');
    const categoryList = document.querySelector('.category-list');
    const categoryMenus = document.querySelectorAll('.category-list a')
    const selectedCategoryDiv = document.querySelector('.selected-category');
    const categoryValue = selectedCategoryDiv.textContent.trim();
    const likeBox = document.querySelector('.like-box');
    const likeButton = document.querySelector('.like');
    const likeImg = document.querySelector('.like img');
    let currentIndex = 0;


    function updateSlider() {
        images.forEach((img, index) => {
            img.style.opacity = index === currentIndex ? '1' : '0';
        });
        const buttons = imageCountBox.querySelectorAll('button');
        buttons.forEach((button, index) => {
            button.className = index === currentIndex ? 'image-circle-button-active' : 'image-circle-button';
        });
        prevButton.style.display = currentIndex === 0 ? 'none' : 'flex';
        nextButton.style.display = currentIndex === images.length - 1 ? 'none' : 'flex';
    }

    function updateModalSlider() {
        modalImage.src = images[currentIndex].src;
        const buttons = modalImageCountBox.querySelectorAll('button');
        buttons.forEach((button, index) => {
            button.className = index === currentIndex ? 'modal-image-circle-button-active' : 'modal-image-circle-button';
        });
    }

    images.forEach((_, index) => {
        const button = document.createElement('button');
        button.className = 'image-circle-button';
        button.addEventListener('mouseenter', () => {
            if (index !== currentIndex) {
                button.style.backgroundColor = '#888888';
            }
        });
        button.addEventListener('mouseleave', () => {
            button.style.backgroundColor = '';
        });
        button.addEventListener('click', () => {
            currentIndex = index;
            updateSlider();
        });
        imageCountBox.appendChild(button);

        const modalButton = document.createElement('button');
        modalButton.className = 'modal-image-circle-button';
        modalButton.addEventListener('click', () => {
            currentIndex = index;
            updateModalSlider();
        });
        modalImageCountBox.appendChild(modalButton);
    });

    prevButton.addEventListener('click', () => {
        if (currentIndex > 0) {
            currentIndex--;
            updateSlider();
        }
    });

    nextButton.addEventListener('click', () => {
        if (currentIndex < images.length - 1) {
            currentIndex++;
            updateSlider();
        }
    });

    zoomButton.addEventListener('click', () => {
        updateModalSlider();
        modal.style.display = 'flex';
    });

    exitModalButton.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    modal.addEventListener('click', (event) => {
        const isClickInsideImageBox = modalImageBox.contains(event.target);
        const isClickInsideCountBox = modalImageCountBox.contains(event.target);
        const isClickInsideExitButton = exitModalButton.contains(event.target);

        if (!isClickInsideImageBox && !isClickInsideCountBox && !isClickInsideExitButton) {
            modal.style.display = 'none';
        }
    });

    productImageWrapper.addEventListener('mouseenter', () => {
        prevButton.style.display = currentIndex === 0 ? 'none' : 'flex';
        nextButton.style.display = currentIndex === images.length - 1 ? 'none' : 'flex';
        zoomButton.style.display = 'flex';
    });

    productImageWrapper.addEventListener('mouseleave', () => {
        prevButton.style.display = 'none';
        nextButton.style.display = 'none';
        zoomButton.style.display = 'none';
    });

    categoryBox.addEventListener('mouseenter', () => {
        categoryList.style.display = 'block';
    });
    categoryBox.addEventListener('mouseleave', () => {
        categoryList.style.display = 'none';
    });

    categoryMenus.forEach((menu, index) => {

        if (categoryValue === menu.textContent.trim()) {
            menu.style.color = 'red';
        }
    });


    updateSlider();
});

$(document).ready(function() {
    $('.like, .unlike').click(function() {
        var button = $(this);
        var itemId = button.data('item-id');
        var userId = button.data('user-id');

        $.ajax({
            url: '/check-login-status',
            type: 'GET',
            success: function (response) {
                if (!response.isLoggedin) {
                    alert("로그인이 필요합니다");
                    window.location.href = '/login?redirect=' + encodeURIComponent(window.location.href);
                } else {
                    $.ajax({
                        url: '/toggle-wish',
                        type: 'POST',
                        data: JSON.stringify({ itemId: itemId, userId: userId}),
                        contentType: 'application/json',
                        success: function(response) {
                            if (response.isWished) {
                                button.removeClass('unlike').addClass('like');
                                button.find('img').attr('src', '/image/wished_icon.png');
                            } else {
                                button.removeClass('like').addClass('unlike');
                                button.find('img').attr('src', '/image/unwished_icon.png');
                            }
                            $('.wish-count').text(response.wishCount);
                            button.find('span.like-count').text(response.wishCount);
                        }
                    });
                }
            },
        });
    });
});

