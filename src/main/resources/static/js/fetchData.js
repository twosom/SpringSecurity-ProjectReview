const fetchData = {
    /**
     *
     * @param url -url을 적어줍니다. 전송하고자 하는 url을 적어주면 됩니다.
     * @param method -메소드를 적어줍니다. HTTP 메소드를 적어주면 됩니다.
     * @param properties -폼 데이터를 전송할 property 의 id만 적어주면 됩니다. <br/>해당 메소드에서 id 값으로 객체를 찾아 그 값을 폼 데이터에 추가합니다.
     */
    async form(url, method, ...properties) {
        const formData = new FormData();
        properties.forEach(property => {
            formData.append(property, document.querySelector('#' + property).value);
        });

        this.option = {
            method: method,
            body: formData
        };
        console.log(this.option);


        return await fetch(url, this.option);
    }
};