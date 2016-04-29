System.register(['angular2/core', 'angular2/http'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, http_1, http_2;
    var AuthenticationService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
                http_2 = http_1_1;
            }],
        execute: function() {
            AuthenticationService = (function () {
                function AuthenticationService(http) {
                    this.http = http;
                    this.loggedIn = false;
                    this.loggedIn = !!localStorage.getItem('auth_token');
                }
                AuthenticationService.prototype.login = function (username, password) {
                    var headers = new http_2.Headers();
                    headers.append('Content-Type', 'application/json');
                    var _resultUrl = '';
                    var string = btoa(username) + ":" + btoa(password);
                    var token = btoa(string);
                    localStorage.setItem('auth_token', token);
                    /*return this.http.post(_resultUrl, JSON.stringify({ username, password }), { headers })
                                    .map(res => res.json())
                                    .map((res) => {
                                            if (res.success) {
                                              var token =btoa(btoa(username) + ":" + btoa(password))
                                              localStorage.setItem('auth_token',token);
                                              this.loggedIn = true;
                                            }
                                            return res.success;
                                    });*/
                };
                AuthenticationService.prototype.logout = function () {
                    localStorage.removeItem('auth_token');
                    this.loggedIn = false;
                    /*
                    return this.http.get(this.config.serverUrl + '/auth/logout', {
                      headers: new Headers({
                        'x-security-token': this.token
                      })
                    })
                    .map((res : any) => {
                      this.token = undefined;
                      localStorage.removeItem('token');
                    });
                     */
                };
                AuthenticationService.prototype.isLoggedIn = function () {
                    return !!localStorage.getItem("auth_token");
                };
                AuthenticationService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http])
                ], AuthenticationService);
                return AuthenticationService;
            }());
            exports_1("AuthenticationService", AuthenticationService);
        }
    }
});
//# sourceMappingURL=authentication.service.js.map