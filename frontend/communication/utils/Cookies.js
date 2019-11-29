export default class Cookies {

    static getCookie(name) {
        var v = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
        return v ? v[2] : null;
    }

    static setCookie(name, value, seconds) {
        var d = new Date;
        d.setTime(d.getTime() + 1000*seconds);
        document.cookie = name + "=" + value + ";path=/;expires=" + d.toGMTString();
    }

    static deleteCookie(name) { setCookie(name, '', -1); }
}