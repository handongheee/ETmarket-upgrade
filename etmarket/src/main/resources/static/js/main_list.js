
$(document).ready(function() {

    const size = 20; // 한 번에 불러올 항목의 수
    let loading = false; // 데이터 로드 중인지 여부를 나타내는 플래그
    let hasMore = true; // 더 불러올 데이터가 있는지 여부
    let page = 0; // 현재 페이지 번호

    // 데이터를 로드하는 함수
    const loadItems = function() {
        if (loading || !hasMore) return; // 이미 로딩 중이거나 더 불러올 데이터가 없으면 새로운 요청을 하지 않음
        loading = true; // 로딩 시작

        $.ajax({
            url: `/items?page=${page}&size=${size}`, // 서버에 요청할 URL
            type: 'GET', // HTTP GET 요청
            dataType: 'json', // 응답 데이터 형식은 JSON
            success: function(data) {
                addItemsToPage(data.content); // 데이터를 페이지에 추가
                hasMore = !data.last; // 더 불러올 데이터가 있는지 여부를 갱신
                if (hasMore) {
                    page++; // 페이지 번호 증가
                }
                loading = false; // 로딩 완료
            },
            error: function(xhr, status, error) {
                console.error('Error fetching items:', error); // 에러 로그 출력
                loading = false; // 로딩 실패
            }
        });
    };

    // 페이지에 항목들을 추가하는 함수
    const addItemsToPage = function(items) {
        items.forEach(item => {
            // 조건에 맞으면 해당 항목을 무시하고 넘어감
            if (item.dealStatus === '거래완료' || item.itemHidden === '숨김') {
                return;
            }

            // 첫 번째 이미지를 가져옴
            const firstImage = item.itemImgs.length > 0 ? item.itemImgs[0].itemImg : '/image/non_img.png';

            // 각 항목에 대해 HTML 문자열을 생성
            const itemHtml =
                `<div class="styled__ProductWrapper-sc-32dn86-1 eCFZgW">
                    <a class="sc-hzNEM dGgfzI" data-pid="${item.id}"
                        href="/items/${item.itemId}"
                        rel="noopener noreferrer"
                        target="_blank">
                        <div class="sc-chbbiW efblGl">
                            <img alt="상품 이미지" height="194"
                                src="${firstImage}" width="194">
                            <div class="styled__BadgeArea-sc-3zkh6z-0 dwFxLs"></div>
                            <div class="sc-fQejPQ emrreW"></div>
                        </div>
                        <div class="sc-kxynE bKyKhs">
                            <div class="sc-cooIXK gEaPXm">${item.itemTitle}</div>
                            <div class="sc-gmeYpB dcncOY">
                                <div class="sc-fcdeBU eTsiJR">${item.itemPrice}</div>
                                <div class="sc-kZmsYB jMaEM"><span>${timeAgo(item.itemUpdateDate)}</span></div>
                            </div>
                        </div>
                    </a>
                </div>`;
            $('.kSnlsd').append(itemHtml); // 생성된 HTML을 .kSnlsd 요소에 추가
        });
    };

    // 초기 데이터 로드
    loadItems();

    // 무한 스크롤을 위한 이벤트 리스너
    $(window).scroll(function() {
        // 사용자가 페이지 하단에 도달하면 데이터를 로드 => window : 현재 보여지는 화면, document : 전체 페이지 화면
        if ($(window).scrollTop() + $(window).height() >= $(document).height() - 500) {
            loadItems();
        }
    });

    // 날짜 차이를 계산하여 사람이 읽을 수 있는 형식으로 반환하는 함수
    const timeAgo = function(date) {
        const now = new Date();
        const updatedDate = new Date(date);
        const diffInSeconds = Math.floor((now - updatedDate) / 1000);
        const diffInMinutes = Math.floor(diffInSeconds / 60);
        const diffInHours = Math.floor(diffInMinutes / 60);
        const diffInDays = Math.floor(diffInHours / 24);

        if (diffInSeconds < 60) {
            return `${diffInSeconds}초 전`;
        } else if (diffInMinutes < 60) {
            return `${diffInMinutes}분 전`;
        } else if (diffInHours < 24) {
            return `${diffInHours}시간 전`;
        } else {
            return `${diffInDays}일 전`;
        }
    };
    //=============== 배너 ==============================
    let currentIndex = 0;
    const slider = $('.kind_slider');
    const slides = slider.children().length;

    $('.next').on('click', function(e) {
        e.preventDefault();
        if (currentIndex < slides - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        slider.css('transform', `translateX(${-currentIndex * 1024}px)`);
    });

    $('.prev').on('click', function(e) {
        e.preventDefault();
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = slides - 1;
        }
        slider.css('transform', `translateX(${-currentIndex * 1024}px)`);
    });


});


