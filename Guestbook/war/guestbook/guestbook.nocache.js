function guestbook(){
  var $wnd_0 = window, $doc_0 = document, $stats = $wnd_0.__gwtStatsEvent?function(a){
    return $wnd_0.__gwtStatsEvent(a);
  }
  :null, scriptsDone, loadDone, bodyDone, base = '', metaProps = {}, values = [], providers = [], answers = [], onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'bootstrap', millis:(new Date()).getTime(), type:'begin'});
  if (!$wnd_0.__gwt_stylesLoaded) {
    $wnd_0.__gwt_stylesLoaded = {};
  }
  if (!$wnd_0.__gwt_scriptsLoaded) {
    $wnd_0.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      result = $wnd_0.external && ($wnd_0.external.gwtOnLoad && $wnd_0.location.search.indexOf('gwt.hybrid') == -1);
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (scriptsDone && loadDone) {
      var iframe = $doc_0.getElementById('guestbook');
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name_0){
          return computePropValue(name_0);
        }
        ;
      }
      guestbook = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, 'guestbook', base);
      $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'moduleStartup', millis:(new Date()).getTime(), type:'end'});
    }
  }

  function computeScriptBase(){
    var thisScript, markerId = '__gwt_marker_guestbook', markerScript;
    $doc_0.write('<script id="' + markerId + '"><\/script>');
    markerScript = $doc_0.getElementById(markerId);
    thisScript = markerScript && markerScript.previousSibling;
    while (thisScript && thisScript.tagName != 'SCRIPT') {
      thisScript = thisScript.previousSibling;
    }
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf('#');
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf('?');
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf('/', Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):'';
    }

    ;
    if (thisScript && thisScript.src) {
      base = getDirectoryOfFile(thisScript.src);
    }
    if (base == '') {
      var baseElements = $doc_0.getElementsByTagName('base');
      if (baseElements.length > 0) {
        base = baseElements[baseElements.length - 1].href;
      }
       else {
        base = getDirectoryOfFile($doc_0.location.href);
      }
    }
     else if (base.match(/^\w+:\/\//)) {
    }
     else {
      var img = $doc_0.createElement('img');
      img.src = base + 'clear.cache.gif';
      base = getDirectoryOfFile(img.src);
    }
    if (markerScript) {
      markerScript.parentNode.removeChild(markerScript);
    }
  }

  function processMetas(){
    var metas = document.getElementsByTagName('meta');
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name_0 = meta.getAttribute('name'), content;
      if (name_0) {
        if (name_0 == 'gwt:property') {
          content = meta.getAttribute('content');
          if (content) {
            var value, eq = content.indexOf('=');
            if (eq >= 0) {
              name_0 = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name_0 = content;
              value = '';
            }
            metaProps[name_0] = value;
          }
        }
         else if (name_0 == 'gwt:onPropertyErrorFn') {
          content = meta.getAttribute('content');
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert('Bad handler "' + content + '" for "gwt:onPropertyErrorFn"');
            }
          }
        }
         else if (name_0 == 'gwt:onLoadErrorFn') {
          content = meta.getAttribute('content');
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert('Bad handler "' + content + '" for "gwt:onLoadErrorFn"');
            }
          }
        }
      }
    }
  }

  function unflattenKeylistIntoAnswers(propValArray, value){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value;
  }

  function computePropValue(propName){
    var value = providers[propName](), allowedValuesMap = values[propName];
    if (value in allowedValuesMap) {
      return value;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value);
    }
    throw null;
  }

  var frameInjected;
  function maybeInjectFrame(){
    if (!frameInjected) {
      frameInjected = true;
      var iframe = $doc_0.createElement('iframe');
      iframe.src = "javascript:''";
      iframe.id = 'guestbook';
      iframe.style.cssText = 'position:absolute;width:0;height:0;border:none';
      iframe.tabIndex = -1;
      $doc_0.body.appendChild(iframe);
      $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'moduleStartup', millis:(new Date()).getTime(), type:'moduleRequested'});
      iframe.contentWindow.location.replace(base + strongName);
    }
  }

  providers['user.agent'] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (ua.indexOf('opera') != -1) {
      return 'opera';
    }
     else if (ua.indexOf('webkit') != -1) {
      return 'safari';
    }
     else if (ua.indexOf('msie') != -1) {
      if (document.documentMode >= 8) {
        return 'ie8';
      }
       else {
        var result_0 = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
        if (result_0 && result_0.length == 3) {
          var v = makeVersion(result_0);
          if (v >= 6000) {
            return 'ie6';
          }
        }
      }
    }
     else if (ua.indexOf('gecko') != -1) {
      var result_0 = /rv:([0-9]+)\.([0-9]+)/.exec(ua);
      if (result_0 && result_0.length == 3) {
        if (makeVersion(result_0) >= 1008)
          return 'gecko1_8';
      }
      return 'gecko';
    }
    return 'unknown';
  }
  ;
  values['user.agent'] = {gecko:0, gecko1_8:1, ie6:2, ie8:3, opera:4, safari:5};
  guestbook.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  guestbook.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'loadExternalRefs', millis:(new Date()).getTime(), type:'end'});
    maybeStartModule();
  }
  ;
  computeScriptBase();
  var strongName;
  if (isHostedMode()) {
    if ($wnd_0.external.initModule && $wnd_0.external.initModule('guestbook')) {
      $wnd_0.location.reload();
      return;
    }
    strongName = 'hosted.html?guestbook';
  }
  processMetas();
  $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'bootstrap', millis:(new Date()).getTime(), type:'selectingPermutation'});
  if (!strongName) {
    try {
      unflattenKeylistIntoAnswers(['opera'], 'A0347B786D02BBBD51E040E57587A9EC.cache.html');
      unflattenKeylistIntoAnswers(['safari'], 'DB470A5F3E84AB083F86CAF8EF1D0EA7.cache.html');
      unflattenKeylistIntoAnswers(['gecko1_8'], '42F1A535162308BD2C7E4D2276162CE7.cache.html');
      unflattenKeylistIntoAnswers(['gecko'], '9A775C723A09EF15FA8073B96254E8D0.cache.html');
      unflattenKeylistIntoAnswers(['ie8'], '11C4B24821C7742690F4669C1DFF7DE5.cache.html');
      unflattenKeylistIntoAnswers(['ie6'], '9CF6B0A385F220771F8671603942066E.cache.html');
      strongName = answers[computePropValue('user.agent')];
    }
     catch (e) {
      return;
    }
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      if (!__gwt_stylesLoaded['gwt/chrome/chrome.css']) {
        var l = $doc_0.createElement('link');
        __gwt_stylesLoaded['gwt/chrome/chrome.css'] = l;
        l.setAttribute('rel', 'stylesheet');
        l.setAttribute('href', base + 'gwt/chrome/chrome.css');
        $doc_0.getElementsByTagName('head')[0].appendChild(l);
      }
      maybeStartModule();
      if ($doc_0.removeEventListener) {
        $doc_0.removeEventListener('DOMContentLoaded', onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc_0.addEventListener) {
    $doc_0.addEventListener('DOMContentLoaded', function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc_0.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'bootstrap', millis:(new Date()).getTime(), type:'end'});
  $stats && $stats({moduleName:'guestbook', subSystem:'startup', evtGroup:'loadExternalRefs', millis:(new Date()).getTime(), type:'begin'});
  $doc_0.write('<script defer="defer">guestbook.onInjectionDone(\'guestbook\')<\/script>');
}

guestbook();
