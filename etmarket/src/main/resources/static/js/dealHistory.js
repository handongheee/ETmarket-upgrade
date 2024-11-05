document.addEventListener('DOMContentLoaded', function () {
    const sellHistoryLink = document.querySelector('.item_status.sell-history');
    const buyHistoryLink = document.querySelector('.item_status.buy-history');
    const historySection = document.querySelector('.TZQfU');
    const ratingModal = document.querySelector('.ratingModal');
    const submitRatingButton = document.querySelector('.submitRatingButton');
    let currentDealId = null;
    let currentReviewerId = null;
    let currentTargetId = null;

    sellHistoryLink.addEventListener('click', function (event) {
        event.preventDefault();
        loadHistory('sell');
        setActiveLink(sellHistoryLink);
    });

    buyHistoryLink.addEventListener('click', function (event) {
        event.preventDefault();
        loadHistory('buy');
        setActiveLink(buyHistoryLink);
    });

    function loadHistory(type) {
        fetch(`/deal/history/${type}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.text();
            })
            .then(html => {
                historySection.innerHTML = html;
                addLeaveRatingButtonEvents();
            })
            .catch(error => console.error('Error:', error));
    }

    function setActiveLink(activeLink) {
        document.querySelectorAll('.item_status').forEach(link => {
            link.classList.remove('active');
        });
        activeLink.classList.add('active');
    }

    function addLeaveRatingButtonEvents() {
        document.querySelectorAll('.leaveRatingButton').forEach(button => {
            button.addEventListener('click', function() {
                const dealPartName = button.dataset.dealPartName;
                const dealIdInput = button.parentElement.querySelector('input[name="dealId"]');
                const userIdInput = button.parentElement.querySelector('input[name="userId"]');

                if (!dealIdInput || !userIdInput) {
                    console.error('필수 입력 필드를 찾을 수 없습니다.');
                    return;
                }

                const dealId = dealIdInput.value;
                const reviewerId = userIdInput.value;

                fetch(`/rating/${dealId}/getTargetId?reviewerId=${encodeURIComponent(reviewerId)}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('서버로부터 거래 상대방 ID를 가져오는 데 실패했습니다.');
                        }
                        return response.json();
                    })
                    .then(data => {
                        const targetId = data.targetId;
                        openRatingModal(dealId, reviewerId, targetId, dealPartName);
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert(error.message);
                    });
            });
        });
    }

    function openRatingModal(dealId, reviewerId, targetId, dealPartName) {
        currentDealId = dealId;
        currentReviewerId = reviewerId;
        currentTargetId = targetId;

        document.querySelector('.ratingModal .dealPartName').textContent = dealPartName;
        ratingModal.style.display = 'block';
    }

    submitRatingButton.addEventListener('click', function () {
        const rating = document.querySelector('.ratingInput').value;
        const comment = document.querySelector('.commentInput').value;

        const ratingData = {
            dealId: currentDealId,
            reviewerId: currentReviewerId,
            targetId: currentTargetId,
            rating: parseFloat(rating),
            comment: comment
        };

        fetch('/rating/leave', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(ratingData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('평가 저장에 실패했습니다.');
                }
                return response.json();
            })
            .then(data => {
                console.log('Success:', data);
                alert('리뷰가 성공적으로 저장되었습니다.');
                ratingModal.style.display = 'none';
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message);
            });
    });

    // 초기 설정
    setActiveLink(sellHistoryLink);
    loadHistory('sell');
});
