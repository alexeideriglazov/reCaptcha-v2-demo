angular.module("reCaptchaDemo").directive("recaptcha", function () {
    return {
        require: "ngModel",
        restrict: "E",
        scope: {
            sitekey: "@",
            ngModel: "="
        },
        link: function (scope, element, attrs, ngModelCtrl) {
            var reCaptcha = document.createElement("script");
            reCaptcha.type = "text/javascript";
            reCaptcha.async = true;
            reCaptcha.src = "https://www.google.com/recaptcha/api.js?onload=onLoadRecaptchaCallback&render=explicit";
            var firstScript = document.getElementsByTagName("script")[0];
            firstScript.parentNode.insertBefore(reCaptcha, firstScript);
            window.onLoadRecaptchaCallback = function () {
                grecaptcha.render(element.get(0), {
                    "sitekey": scope.sitekey,
                    "callback": onRecaptchaSubmit,
                    "expired-callback": onRecaptchaExpired
                });
            };
            window.onRecaptchaSubmit = function (gRecaptchaResponse) {
                scope.ngModel = gRecaptchaResponse;
                ngModelCtrl.$setViewValue(gRecaptchaResponse);
            };
            window.onRecaptchaExpired = function () {
                scope.ngModel = "";
                ngModelCtrl.$setViewValue("");
            };

            ngModelCtrl.$validators.recaptchaValidate = function (modelValue, viewValue) {
                return !ngModelCtrl.$isEmpty(scope.ngModel);
            }
        }

    }
})