/* 게시물 올린 시간 변경(ex.방금전,1시간 전..) */
document.addEventListener('DOMContentLoaded', function () {
    const itemResistDateElements = document.querySelectorAll('.itemResistDate');
    itemResistDateElements.forEach(itemResistDateElement => {
        let itemResistDate = itemResistDateElement.textContent;

        // 문자열을 ISO 8601 형식으로 변환
        itemResistDate = itemResistDate.replace(/\s+/g, 'T').split('.')[0];

        itemResistDateElement.textContent = timeForToday(itemResistDate);
    });
});

function timeForToday(value) {
    const today = new Date();
    const timeValue = new Date(value);

    const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
    if (betweenTime < 1) return '방금 전';
    if (betweenTime < 60) {
        return `${betweenTime}분 전`;
    }

    const betweenTimeHour = Math.floor(betweenTime / 60);
    if (betweenTimeHour < 24) {
        return `${betweenTimeHour}시간 전`;
    }

    const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
    if (betweenTimeDay < 365) {
        return `${betweenTimeDay}일 전`;
    }

    return `${Math.floor(betweenTimeDay / 365)}년 전`;
}


/* 거래상태 */
document.addEventListener('DOMContentLoaded', function() {
    // 페이지 로드 시 초기화
    document.querySelectorAll('.dropdown-select').forEach(select => {
        const itemId = select.getAttribute('data-itemid');
        const dealStatus = select.value;
        updateButtonVisibility(itemId, dealStatus); // 초기화 시 버튼 상태 업데이트

        // 드롭다운 선택 값 변경 시 이벤트 리스너 추가
        select.addEventListener('change', function() {
            updateDealStatus(this); // 선택 값 변경 시 상태 업데이트
        });
    });
});

function updateDealStatus(selectElement) {
    const selectedValue = selectElement.value;
    const itemId = selectElement.getAttribute('data-itemid');
    console.log(`Status updated - Item ID: ${itemId}, Selected Value: ${selectedValue}`);

    fetch(`/deal/status/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ itemId: itemId, dealStatus: selectedValue })
    })
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                console.error('Update failed:', data.message);
            } else {
                updateButtonVisibility(itemId, selectedValue); // 상태 업데이트 후 버튼 상태 업데이트
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateButtonVisibility(itemId, selectedValue) {
    const buttonContainer = document.querySelector(`.status-button-container[data-itemid="${itemId}"]`);
    let button = buttonContainer.querySelector('.status-button');

    if (!button) {
        // 버튼이 없는 경우, 생성
        button = document.createElement('button');
        button.className = 'status-button';
        buttonContainer.appendChild(button);
    }

    if (selectedValue === '예약중' || selectedValue === '거래완료') {
        button.style.display = 'block';
        button.textContent = selectedValue;
        button.classList.remove('button-green', 'button-black'); // 기존 클래스 제거
        button.classList.add(`button-${selectedValue === '예약중' ? 'green' : 'black'}`); // 새 클래스 추가
    } else {
        button.style.display = 'none';
    }
}




/*끌어올리기*/
document.addEventListener('DOMContentLoaded', function () {
    const upButtons = document.querySelectorAll('.upButton');

    upButtons.forEach(button => {
        button.addEventListener('click', function () {
            const itemId = button.getAttribute('data-itemid');
            const userId = button.getAttribute('data-userid');

            fetch('/items/up', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({itemId: itemId, userId: userId})
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();  // 응답을 JSON으로 처리
                    } else {
                        throw new Error('끌어올리기에 실패했습니다.');
                    }
                })
                .then(data => {
                    if (data.remainingUpCount > 0) {
                        alert(`하루에 남은 끌어올리기 횟수는 ${data.remainingUpCount}번입니다.`);
                    } else {
                        alert('하루에 더 이상 끌어올리기를 할 수 없습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    /*alert(`클릭된 버튼의 itemId: ${itemId}, userId: ${userId}`);*/
                    alert('끌어올리기에 실패했습니다.');
                });
        });
    });
});

/* 끌어올리기 횟수 */
$(document).ready(function() {
    $('.itemUpCount').each(function () {
        const $this = $(this);
        const itemId = $this.attr('data-itemid');

        $.ajax({
            url: '/items/up-count/' +itemId, // 경로에 itemId를 포함하지 않음
            type: 'GET',
            success: function (data) {
                const upCount = data; // 받은 데이터에서 끌어올림 횟수 가져오기
                if (upCount > 0) {
                    $this.text(' · 끌올 ' + upCount + '회');
                }else{
                    $this.text(' ');
                }
            },
            error: function () {
                $this.text('끌어올림 횟수 조회 실패');
                console.log(itemId);
            }
        });
    });
});


/* 숨기기 */
document.addEventListener('DOMContentLoaded', function () {
// 페이지 로드 시 저장된 상태를 가져와 버튼 상태 설정
    document.querySelectorAll('.hideButton').forEach(function (button) {
        const itemId = button.getAttribute('data-itemid');
        const storedStatus = localStorage.getItem('hiddenStatus_' + itemId);
        if (storedStatus) {
            button.setAttribute('data-status', storedStatus);
            button.textContent = storedStatus === '숨김' ? '숨기기 해제' : '숨기기';
        }

        button.addEventListener('click', function () {
            const currentStatus = this.getAttribute('data-status');
            const itemId = this.getAttribute('data-itemid');
            toggleHiddenStatus(this, itemId, currentStatus);
        });
    });
});

function toggleHiddenStatus(button, itemId, currentStatus) {
    if (!itemId) {
        console.error('Item ID is undefined');
        alert('아이템 ID가 정의되지 않았습니다.');
        return;
    }

    const newStatus = currentStatus === '숨김' ? '보임' : '숨김';
    const confirmationMessage = newStatus === '숨김' ? "게시물을 숨기겠습니까?" : "게시물을 다시 보이겠습니까?";

    if (confirm(confirmationMessage)) {
        fetch('/updateHiddenStatus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `itemId=${itemId}&hidden=${newStatus}`
        })
            .then(response => response.json())
            .then(data => {
                alert(data.message);

                // 버튼 텍스트 업데이트
                button.textContent = newStatus === '숨김' ? '숨기기 해제' : '숨기기';
                button.setAttribute('data-status', newStatus);

                // 상태를 로컬 스토리지에 저장
                localStorage.setItem('hiddenStatus_' + itemId, newStatus);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('상태 업데이트에 실패했습니다.');
            });
    }
}



/*document.addEventListener("DOMContentLoaded", function() {
    var clickedDivId = localStorage.getItem('clickedDivId');
    if (clickedDivId) {
        document.getElementById(clickedDivId).classList.add('clicked');
    }
});*/

function changeColor(element) {
    // 모든 div에서 clicked 클래스를 제거
    var divs = document.querySelectorAll('.sc-bJTOcE');
    divs.forEach(function(div) {
        div.classList.remove('clicked');
    });

    // 클릭된 div에 clicked 클래스를 추가하고 로컬 스토리지에 저장
    element.classList.add('clicked');

    // element의 고유 식별자를 localStorage에 저장
    localStorage.setItem('clickedDivId', element.dataset.itemId);
}


/*거래상태가 거래완료시 구매자 선택 후 거래내역에 insert*/
function updateItemStatus(selectElement) {
    if (selectElement.value === '거래완료') {
        const itemId = selectElement.getAttribute('data-itemid');
        fetch(`/deal/${itemId}/chatParticipants`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const buyerList = document.querySelector('.buyer-list');
                buyerList.innerHTML = '';
                data.forEach(user => {
                    const li = document.createElement('li');
                    li.textContent = user.userName;
                    li.onclick = () => selectBuyer(li, user); // li 요소와 user를 전달
                    buyerList.appendChild(li);
                });
                document.querySelector('.buyer-modal').style.display = 'block';
            })
            .catch(error => console.error('Error fetching chat participants:', error));
    } else {
        // 다른 상태 처리 로직
    }
}

function closeModal() {
    document.querySelector('.buyer-modal').style.display = 'none';
    selectedBuyerElement = null; // 모달을 닫을 때 선택된 항목 초기화
}

let selectedBuyer = null;
let selectedBuyerElement = null; // 선택된 li 요소를 추적

function selectBuyer(li, user) {
    if (selectedBuyerElement) {
        selectedBuyerElement.classList.remove('selected'); // 이전에 선택된 항목의 스타일 제거
    }
    selectedBuyer = {
        id: user.userId,
        name: user.userName
    };
    selectedBuyerElement = li;
    li.classList.add('selected'); // 선택된 항목에 스타일 적용

    console.log('Received user object:', selectedBuyer);
}

function confirmBuyer() {
    if (selectedBuyer) {
        console.log('선택된 구매자 ID:', selectedBuyer);
        const itemId = document.querySelector('.dropdown-select[data-itemid]').getAttribute('data-itemid');
        fetch(`/deal/${itemId}/complete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ buyerId: selectedBuyer.id })
        })
            .then(response => {
                if (response.ok) {
                    closeModal();
                    alert('거래가 완료되었습니다.');
                    // 추가적으로 필요한 작업 (예: 페이지 새로고침)
                } else {
                    alert('거래 완료에 실패했습니다.');
                }
            })
            .catch(error => console.error('Error completing deal:', error));
    } else {
        alert('구매자를 선택해주세요.');
    }
}
