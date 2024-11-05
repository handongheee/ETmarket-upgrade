// 주소검색 API
function sample4_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var roadAddr = data.roadAddress;

            document.getElementById("sample4_roadAddress").value = roadAddr;

            var guideTextBox = document.getElementById("guide");
            if (data.autoRoadAddress) {
                var expRoadAddr = data.autoRoadAddress;
                guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                guideTextBox.style.display = 'block';
            } else if (data.autoJibunAddress) {
                var expJibunAddr = data.autoJibunAddress;
                guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                guideTextBox.style.display = 'block';
            } else {
                guideTextBox.innerHTML = '';
                guideTextBox.style.display = 'none';
            }
        }
    }).open();
}

// 제출 버튼 클릭 시 이벤트 처리
document.getElementById('itemInsertForm').addEventListener('submit', function(event) {
    // 모든 requiredInput 속성을 가진 input과 textarea 요소를 선택합니다.
    const requiredInputs = document.querySelectorAll('input[requiredInput], textarea[requiredInput]');
    const errorMessages = document.querySelectorAll('span.errorMessage');

    //기존 errorMessage 숨기기
    errorMessages.forEach(function(element) {
        element.style.display = 'none';
    });

    // for 메서드를 사용하여 각 요소를 순회하며 값이 비어있는지 확인합니다.
    let scrollToElement = null;
    for(let i=0; i<requiredInputs.length; i++) {
        let requiredInput = requiredInputs[i];
        let errorMessage = errorMessages[i];
        if (requiredInput.value.trim() === '') {
            scrollToElement = requiredInput;
            errorMessage.style.display = 'block';
            break; // 값이 비어있으면 바로 탐색 종료
        }
    }

    // scrollToElement가 설정되어 있다면 해당 요소로 스크롤 이동
    if (scrollToElement) {
        scrollToElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
        event.preventDefault(); // 이벤트 전파를 막습니다. (폼 제출을 중지시킵니다.)
    }
});

//숫자만 입력가능하고 1000단위로 ,붙혀줌
function formatNumber(input) {
    let value = input.value;

    // 첫 번째 문자가 0인 경우 처리
    if (value.startsWith('0')) {
        value = ''; // 0을 삭제
    }

    // 입력된 값에서 숫자를 제외한 모든 문자 제거
    value = value.replace(/[^\d]/g, '');

    // 천 단위로 쉼표 추가
    let parts = value.split('.');
    let number = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');

    // 정수만 입력을 위해 소수점 이하 값 제거
    input.value = number;
}

//카테고리 버튼 입력시 input에 값입력
function fillInputCategory(button) {
    // 버튼의 텍스트 값을 가져와서 입력 필드에 설정
    var inputValue = button.textContent.trim();
    document.getElementById('categoryName').value = inputValue;

    // 모든 버튼에서 active 클래스 제거
    var buttons = document.querySelectorAll('.cVEXWs');
    buttons.forEach(function(btn) {
        btn.classList.remove('active');
    });

    // 클릭된 버튼에 active 클래스 추가
    button.classList.add('active');
}

//상품상태 선택시 input에 값입력
function fillHiddenInputItemStatus(radio) {
    var radioValue = radio.value;
    document.getElementById('itemStatus').value = radioValue;
}

//태그 input 조건
function tagInputLimit(input) {
    let value = input.value;

    // 조건 1: 띄어쓰기는 4번까지만 가능
    const spaceCount = (value.match(/ /g) || []).length;
    if (spaceCount > 4) {
        const words = value.split(' ');
        value = words.slice(0, 5).join(' ');
    }

    // 조건 2: 첫번째 글자는 띄어쓰기 불가능
    if (value.startsWith(' ')) {
        value = value.trimStart();
    }

    // 조건 3: 단어 1개의 최대글자수는 9자
    const words = value.split(' ').map(word => {
        if (word.length > 9) {
            return word.substring(0, 9);
        }
        return word;
    });

    // 조건 4: 연속된 띄어쓰기 2번 이상 금지
    value = words.join(' ').replace(/\s{2,}/g, ' ');

    input.value = value;
}

//배송비포함여부 선택시 input에 값입력
function fillHiddenInputDeliveryStatus(radio) {
    var radioValue = radio.value;
    document.getElementById('deliveryStatus').value = radioValue;

    var itemDeliveryPriceInput = document.getElementById('itemDeliveryPriceInput');

    if (radioValue === '비포함') {
        itemDeliveryPriceInput.style.display = 'block';
        document.getElementById('itemDeliveryPriceText').value = null;
    } else {
        itemDeliveryPriceInput.style.display = 'none';
        document.getElementById('itemDeliveryPriceText').value = 0;
    }
}

//직거래가능여부 선택시 input에 값입력
function fillHiddenInputItemDealHow(radio) {
    var radioValue = radio.value;
    document.getElementById('dealHow').value = radioValue;
}

// 이미지 업로드시 미리보기 생성 및 동기화
function previewImages(event) {
    const imagePreviewList = document.getElementById('imagePreviewList');

    // eYzyqx 클래스를 가진 li 요소들을 선택하여 초기화합니다.
    const itemsToRemove = imagePreviewList.querySelectorAll('li.eYzyqx');
    itemsToRemove.forEach(item => {
        item.remove(); // 요소를 제거하거나 다른 초기화 작업을 수행할 수 있습니다.
    });

    document.querySelector("input[name='itemDeleteImgIds']").value = "";

    const files = event.target.files;

    // 파일을 읽고 결과를 반환하는 Promise를 생성합니다.
    const readFile = (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result);
            reader.onerror = reject;
            reader.readAsDataURL(file);
        });
    };

    // 모든 파일을 읽고 나서 처리합니다.
    Promise.all(Array.from(files).map(readFile))
        .then(fileImgs => {
            fileImgs.forEach((fileImg, index) => {
                const li = document.createElement('li');
                li.classList.add('eYzyqx');

                const div = document.createElement("div");
                div.classList.add('imMXYo');
                div.textContent = "대표 이미지";

                const img = document.createElement('img');
                img.src = fileImg;

                const btn = document.createElement('button');
                btn.classList.add('cRcpOX');
                btn.addEventListener('click', function() {
                    // event.target.files에서 해당 파일을 제거합니다.
                    const newFiles = Array.from(event.target.files).filter((f, i) => i !== index);

                    // 변경된 파일 목록으로 새 FileList 객체를 생성합니다.
                    const newFileList = new DataTransfer();
                    newFiles.forEach(f => newFileList.items.add(f));

                    // input 요소의 files 속성을 업데이트합니다.
                    event.target.files = newFileList.files;

                    // 변경된 event.target.files로 미리보기를 다시 생성합니다.
                    previewImages(event);
                });

                li.appendChild(div);
                li.appendChild(img);
                li.appendChild(btn);

                imagePreviewList.appendChild(li);
            });

            // 대표 이미지 표시
            if (fileImgs.length > 0) {
                document.querySelector('div.imMXYo').style.display = 'flex';
            }
        })
        .catch(error => {
            console.error('파일을 읽는 중 오류가 발생했습니다:', error);
        });
}

// 카테고리 버튼을 클릭하는 함수
function clickCategoryButton(categoryName) {
    // 모든 버튼을 가져옴
    var buttons = document.querySelectorAll("button.cVEXWs");

    // 각 버튼의 텍스트와 category를 비교하여 일치하면 클릭
    buttons.forEach(function(button) {
        if (button.textContent.trim() === categoryName) {
            button.click();
        }
    });
}

clickCategoryButton(document.getElementById("categoryName").value);

// 상품상태 버튼을 선택하는 함수
function selectItemStatusButton(itemStatus) {
    // 모든 라디오 버튼을 가져옴
    var radios = document.querySelectorAll("input[name='itemStatusSelector']");

    // 각 라디오 버튼의 value와 itemStatus를 비교하여 일치하면 선택
    radios.forEach(function(radio) {
        if (radio.value === itemStatus) {
            radio.click();
        }
    });
}

selectItemStatusButton(document.getElementById("itemStatus").value);

// 배송비포함여부 버튼을 선택하는 함수
function selectDeliveryStatusButton(deliveryStatus) {
    // 모든 라디오 버튼을 가져옴
    var radios = document.querySelectorAll("input[name='deliveryStatusSelector']");
    var itemDeliveryPriceText = document.getElementById("itemDeliveryPriceText").value;

    // 각 라디오 버튼의 value와 itemStatus를 비교하여 일치하면 선택
    radios.forEach(function(radio) {
        if (radio.value === deliveryStatus) {
            radio.click();
            document.getElementById("itemDeliveryPriceText").value = itemDeliveryPriceText;
        }
    });
}

selectDeliveryStatusButton(document.getElementById("deliveryStatus").value);

// 직거래여부 버튼을 선택하는 함수
function selectDealHowButton(dealHow) {
    // 모든 라디오 버튼을 가져옴
    var radios = document.querySelectorAll("input[name='dealHowSelector']");

    // 각 라디오 버튼의 value와 itemStatus를 비교하여 일치하면 선택
    radios.forEach(function(radio) {
        if (radio.value === dealHow) {
            radio.click();
        }
    });
}

selectDealHowButton(document.getElementById("dealHow").value);

// 이미지 미리보기 제거 함수
function removeImage(event, index) {
    const liToRemove = document.querySelectorAll('.eYzyqx')[index];
    const button = event.target;
    const imgId = button.value;
    let deleteItemIds = document.querySelector("input[name=itemDeleteImgIds]").value;
    let count = document.querySelector("input[name=itemImgUploadCount]").value;

    if (count == 1) {
        alert("최소 1개의 이미지가 필요합니다.");
    } else {
        liToRemove.remove();

        deleteItemIds = deleteItemIds + " " + imgId;
         deleteItemIds = deleteItemIds.trim();
        document.querySelector("input[name=itemDeleteImgIds]").value = deleteItemIds;

        count--;
        document.querySelector("input[name=itemImgUploadCount]").value = count;

        // li 제거 후 나머지 요소들의 인덱스를 업데이트
        updateRemoveImageButtons();

        document.querySelector('div.imMXYo').style.display = 'flex';
    }
}

// 이미지 미리보기 제거후 인덱스를 다시 할당하는 함수
function updateRemoveImageButtons() {
    const buttons = document.querySelectorAll('.cRcpOX');
    buttons.forEach((button, index) => {
        button.setAttribute('onclick', 'removeImage(event, ' + index + ')');
    });
}