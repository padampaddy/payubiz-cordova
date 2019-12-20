#import <Cordova/CDVPlugin.h>

@interface PayUBizCordova : CDVPlugin

- (void)showPaymentView:(CDVInvokedUrlCommand*)command;

@end