/**
 *  DSL 형식으로 fetch API 를 사용할 수 있게 재정의함.
 */
const fetchDsl = {
    option: {
        url: '',
        method: 'GET',
        header: {}
    },
    /**
     *
     * @param url 대상이 되는 url 을 적어줍니다.
     */
    url(url) {
        this.option.url = url;
        return this;
    },
    /* METHOD */
    /**
     * HttpMethod 의 POST 방식으로 설정합니다.
     */
    post() {
        this.option.method = 'POST';
        return this;
    },
    /**
     * HttpMethod 의 GET 방식으로 설정합니다.
     */
    get() {
        this.option.method = 'GET';
        return this;
    },
    /**
     * HttpMethod 의 PUT 방식으로 설정합니다.
     */
    put() {
        this.option.method = 'PUT';
        return this;
    },
    /**
     * HttpMethod 의 DELETE 방식으로 설정합니다.
     */
    delete() {
        this.option.method = 'DELETE';
        return this;
    },

    /**
     *
     * @param headers 설정하고자 하는 헤더 값들을 정의합니다.
     */
    headers(headers) {
        this.option.headers = headers;
        return this;
    },
    initForm(properties) {
        const formData = new FormData();
        properties.forEach((property) => {
            formData.append(property, document.querySelector('#' + property).value);
        });
        this.option.body = formData;
    },
    /**
     * Form 전송 방식으로 설정합니다.
     * @param properties 파라미터로 폼 전송방식에 사용할 프로퍼티 ID 들을 문자열 타입으로 넣어줍니다.<br/>
     * '#' 같은 프리픽스를 붙일 필요 없이 ID 값만 적어줍니다.
     */
    async form(...properties) {
        this.initForm(properties);
        return await fetch(this.option.url, this.option);
    },
    initJson(properties) {
        const data = {};
        let warnCount = 0;

        const iter = properties[Symbol.iterator]();
        for (const property of iter) {
            const node = document.querySelector('#' + property);
            if (node.value.length === 0 || node.value === '') {
                node.setAttribute('class', 'form-control is-invalid');
                const feedbackEl = document.querySelector('#' + property + 'Feedback');
                feedbackEl.setAttribute('class', 'invalid-feedback');
                feedbackEl.innerText = property + ' must not be empty';
                warnCount += 1;
            } else {
                node.setAttribute('class', 'form-control');
                const feedbackEl = document.querySelector('#' + property + 'Feedback');
                feedbackEl.innerText = '';
            }
            data[property] = node.value;
        }

        if (warnCount > 0) {
            throw new Error('empty');
        }
        this.option.body = JSON.stringify(data);
    },
    /**
     * Ajax 방식으로 설정합니다.
     * 내부에서 JSON 으로 변환 후 전송합니다.
     * @param properties JSON 으로 변환할 객체의 ID 값만 넘겨줍니다.<br/>
     * 내부에서 JSON 타입으로 변환 후 전송합니다.
     */
    async json(...properties) {
        if (properties.length > 0) {
            this.initJson(properties);
        }
        const response = await fetch(this.option.url, this.option);
        // if (response.status === 200) {
        //     if (response.url.includes('login')) {
        //         throw response;
        //     }
        // }

        if (response.status !== 200) {
            const result = {};
            switch (response.status) {
                case 401:
                    result.status = 401;
                    result.json = await response.json();
                    throw result;
                case 403:
                    result.status = 403;
                    result.json = await response.json();
                    throw result;

                default:
                    throw await response.text();
            }
        }
        return await response.text();
    }
}