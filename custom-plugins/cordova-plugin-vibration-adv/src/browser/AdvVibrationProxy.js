// src/browser/AdvVibrationProxy.js
var execProxy = require('cordova/exec/proxy');

function isArrayOfInts(a){ return Array.isArray(a) && a.every(n => Number.isInteger(n) && n >= 0); }

execProxy.add('AdvVibration', {
  vibratePattern: function (success, error, args) {
    try {
      var pattern = (args && args[0]) || [];
      if (!isArrayOfInts(pattern)) {
        error && error('Pattern must be array of non-negative integers');
        return;
      }
      if (navigator.vibrate) {
        navigator.vibrate(pattern);
        console.log('[AdvVibration(browser)] vibrate', pattern);
        success && success();
      } else {
        console.warn('[AdvVibration(browser)] navigator.vibrate not supported');
        error && error('navigator.vibrate not supported');
      }
    } catch (e) {
      console.error('[AdvVibration(browser)] error', e);
      error && error(e.message || String(e));
    }
  }
});
