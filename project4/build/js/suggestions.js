/**
 * Provides suggestions.
 * @class
 * @scope public
 */
var xhr = new XMLHttpRequest();

function SuggestionList() {}

SuggestionList.prototype.sendAjaxRequest = function (oAutoSuggestControl, bTypeAhead) {
   var sTextboxValue = oAutoSuggestControl.textbox.value;
       var request = "/eBay/suggest?q=" + encodeURI(sTextboxValue);
       xhr.open("GET", request);
       xhr.onreadystatechange = this.requestSuggestions(oAutoSuggestControl, bTypeAhead);
       xhr.send(null);

};

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
SuggestionList.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {

    var aSuggestions = [];
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    var request = "/eBay/suggest?q=" + encodeURI(sTextboxValue);
    xhr.open("GET", request);
    xhr.onreadystatechange = function() {
         if (xhr.readyState == 4) {
             var s = xhr.responseXML.getElementsByTagName('CompleteSuggestion');

             //search for matching suggestion
             for (var i=0; i < s.length; i++) {
                 var text = s[i].childNodes[0].getAttribute("data");
                 aSuggestions.push(text);
             }
         }
         //provide suggestions to the control
         oAutoSuggestControl.autosuggest(aSuggestions, false);
    }

    xhr.send(null);

};