System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Search;
    return {
        setters:[],
        execute: function() {
            Search = (function () {
                function Search(id, text) {
                    this.id = id;
                    this.text = text;
                }
                return Search;
            }());
            exports_1("Search", Search);
        }
    }
});
//# sourceMappingURL=search.js.map