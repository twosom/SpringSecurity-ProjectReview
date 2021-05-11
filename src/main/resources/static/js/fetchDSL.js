
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
    }
}