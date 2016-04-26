System.register(['angular2/core', 'angular2/router', './scripts/login/login.component', './scripts/register/register.component', './scripts/search/search-form.component', './scripts/result/result.component'], function(exports_1, context_1) {
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
    var core_1, router_1, login_component_1, register_component_1, search_form_component_1, result_component_1;
    var AppComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (login_component_1_1) {
                login_component_1 = login_component_1_1;
            },
            function (register_component_1_1) {
                register_component_1 = register_component_1_1;
            },
            function (search_form_component_1_1) {
                search_form_component_1 = search_form_component_1_1;
            },
            function (result_component_1_1) {
                result_component_1 = result_component_1_1;
            }],
        execute: function() {
            AppComponent = (function () {
                function AppComponent() {
                    this.libelleImageUrl = 'app/assets/files/libelle.jpg';
                    this.logoImageUrl = 'app/assets/files/logo.jpg';
                }
                AppComponent = __decorate([
                    core_1.Component({
                        selector: 'storii-app',
                        templateUrl: "app/html/start/start.html",
                        directives: [router_1.ROUTER_DIRECTIVES],
                        providers: [router_1.ROUTER_PROVIDERS]
                    }),
                    router_1.RouteConfig([
                        { path: '/', name: 'Search', component: search_form_component_1.SearchFormComponent, useAsDefault: true },
                        { path: '/login', name: 'Login', component: login_component_1.LoginComponent },
                        { path: '/register', name: 'Register', component: register_component_1.RegisterComponent },
                        { path: '/search', name: 'Result', component: result_component_1.ResultComponent }
                    ]), 
                    __metadata('design:paramtypes', [])
                ], AppComponent);
                return AppComponent;
            }());
            exports_1("AppComponent", AppComponent);
        }
    }
});
//# sourceMappingURL=app.component.js.map