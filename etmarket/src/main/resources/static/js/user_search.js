
$(document).ready(function() {

    // 페이지 로드 시 바로 호출
    init();

    // $('#items').hide();

    // 회원 검색 기록
    function init(){
        let userId = 1;

        $.ajax({
            url: '/search/init?userId='+userId,
            type: 'POST',
            contentType: 'application/json',
            success: function (response) {
                console.log("Initial search content:", response);
                top8_search(response);
            },
            error: function (xhr, status, error) {
                console.error("Error loading initial content:", error);
                console.error("Status:", status);
                console.error("XHR response text:", xhr.responseText);
            }
        })
    }

    // 검색 후......
    function search() {
        let query = $('#searchInput').val();
        if (query) {
            // 여기에 검색 로직을 추가하세요.
            insertContent(query);
            console.log("Query inserted, now fetching items...");
            searchUpdate(query);
            location.href="/search?content="+query;

        } else {
            alert('Please enter a search term.');
        }
    }

    // 검색 시 이미 데이터가 존재한다면 시간만 update
    function searchUpdate(content) {
        let data = {
            userId: 1, // 의미로 설정
            content: content
        };
        alert("content : " + content);
        $.ajax({
            url: '/search/update',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (response){
                // $('#searchInput').val(content);

                if (response.status === "success") {
                    alert("Content updated successfully");
                    $('#searchInput').val(response.updatedContent);
                } else {
                    alert("Error: " + response.message);
                }

            },
            error: function (xhr, status, error) {
                console.error("Error occurred while fetching items: ", status, error);
            }
        });
    }

    // 검색 데이터 insert
    function insertContent(content){
        let data = {
            userId: 1, // 의미로 설정
            content: content
        };
        $.ajax({
            url: '/search/insert',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response){
                console.log("Content inserted");
                // init();
            }
        })
    }

    // 검색 데이터 모두 삭제
    window.deleteAllContent = function () {
        let userId = 1;

        $.ajax({
            url: '/search/deleteall',
            type: 'POST',
            contentType: 'application/json',
            data:  JSON.stringify({"userId":userId}),
            success: function (response){
                alert("delete All");
                init();
            }
        })
    }

    // 회원 검색 기록 html에 출력
    function top8_search(data) {
        let container = $('.bCNTNM');
        container.empty();

        data.forEach(function (item, index){
            var str = '<div class="fcMDtU">' +
                '<button type="button" id="content' + index + '" onclick="getContent(this)">' + item.content + '</button>' +
                '<button type="button" class="gUWbGN" value="'+item.content+'" onclick="deleteContent(this)" ><i class="bi bi-x"></i></button>' +
                '</div>'

            container.append(str);
        })
    }

    // 검색창 엔터 키 이벤트
    $('#searchInput').keypress(function(event) {
        if (event.which === 13) { // 13은 엔터 키의 키 코드입니다.
            console.log("엔터 인식됨");
            search();
        }
    });

    // 돋보기 클릭 이벤트
    $('#searchButton').click(function() {
        console.log("클릭 인식됨");
        search();
    });

    // 돋보기 클릭 이벤트
    $('.bprumR').click(function() {
        console.log("전체 삭제");
        // search();
    });

    // 검색 데이터 클릭 이벤트
    window.getContent = function(contentBtn){
        let content = $(contentBtn).text();
        console.log("검색 목록 content : " + content);
        searchUpdate(content);
        location.href="/search?content="+content;

    }

    // 검색 데이터 delete
    window.deleteContent = function(btn){
        let content = $(btn).val();
        $.ajax({
            url: '/search/delete',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({"content": content}),
            success: function (response){
                console.log("Content Delete success");
                location.reload(true);
            },
            error: function (xhr, status, error) {
                console.error("Error deleting content:", error);
                console.error("Status:", status);
                console.error("XHR response text:", xhr.responseText);
            }
        })
    }

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
});


