System.register(['angular2/core', 'angular2/router', '../search/search.service', '../logState/logState.component', '../../headerfct', '../login/authentication.service'], function(exports_1, context_1) {
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
    var core_1, router_1, search_service_1, logState_component_1, headerfct_1, authentication_service_1;
    var ResultComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (search_service_1_1) {
                search_service_1 = search_service_1_1;
            },
            function (logState_component_1_1) {
                logState_component_1 = logState_component_1_1;
            },
            function (headerfct_1_1) {
                headerfct_1 = headerfct_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            }],
        execute: function() {
            ResultComponent = (function () {
                function ResultComponent(_router, _searchService, _routeParams) {
                    this._router = _router;
                    this._searchService = _searchService;
                    this._routeParams = _routeParams;
                    this.title = 'Search:';
                    this.resultsUsers = 'Users:';
                    this.resultsStories = 'Stories:';
                    this.defaultUserPic = 'app/assets/files/dummyProfile.jpg';
                    this.defaultStoryPic = 'app/assets/files/dummyStory.jpg';
                }
                ResultComponent.prototype.ngOnInit = function () {
                    var cell = document.getElementById('inputField');
                    cell.focus();
                    var value = this._routeParams.get('key');
                    this.resValue = value;
                    this.doSearch(value);
                };
                ResultComponent.prototype.search = function (term) {
                    this._router.navigate(['Result', { key: term }]);
                };
                ResultComponent.prototype.gotoStory = function (storyname) {
                    var _this = this;
                    var user_id = storyname['parentUser'];
                    this._searchService.searchUserById(user_id)
                        .subscribe(function (targetName) {
                        if (targetName) {
                            _this.targetName = targetName;
                            _this._router.navigate(['About', { name: _this.targetName['name'], storyName: storyname['name'], id: storyname['id'] }]);
                        }
                    }, function (error) { return _this.errorMessage = error; });
                };
                ResultComponent.prototype.gotoUser = function (user) {
                    this._router.navigate(['Profile', { name: user['name'] }]);
                };
                ResultComponent.prototype.doSearch = function (term) {
                    var _this = this;
                    if (term != "") {
                        this._searchService.searchStory(term)
                            .subscribe(function (stories) { return _this.stories = stories; }, function (error) { return _this.errorMessage = error; });
                        this._searchService.searchUser(term)
                            .subscribe(function (users) { return _this.users = users; }, function (error) { return _this.errorMessage = error; });
                    }
                };
                ResultComponent = __decorate([
                    core_1.Component({
                        selector: 'result',
                        templateUrl: "app/html/result/result.html",
                        styles: ['a {cursor: pointer}'],
                        directives: [logState_component_1.LogStateComponent],
                        providers: [authentication_service_1.AuthenticationService, search_service_1.SearchService, headerfct_1.HttpClient]
                    }), 
                    __metadata('design:paramtypes', [router_1.Router, search_service_1.SearchService, router_1.RouteParams])
                ], ResultComponent);
                return ResultComponent;
            }());
            exports_1("ResultComponent", ResultComponent);
        }
    }
});
//# sourceMappingURL=result.component.js.map