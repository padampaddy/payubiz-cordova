var exec = require('cordova/exec');

exports.showPaymentView = function (success, error, txnParams) {
    exec(success, error, 'PayUBizCordova', 'showPaymentView', [txnParams]);
};


