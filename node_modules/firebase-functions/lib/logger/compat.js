"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const common_1 = require("./common");
const util_1 = require("util");
/** @hidden */
function patchedConsole(severity) {
    return function (data, ...args) {
        if (common_1.SUPPORTS_STRUCTURED_LOGS) {
            common_1.UNPATCHED_CONSOLE[common_1.CONSOLE_SEVERITY[severity]](JSON.stringify({ severity, message: util_1.format(data, ...args) }));
            return;
        }
        common_1.UNPATCHED_CONSOLE[common_1.CONSOLE_SEVERITY[severity]](data, ...args);
    };
}
// IMPORTANT -- "../logger" must be imported before monkeypatching!
console.debug = patchedConsole('DEBUG');
console.info = patchedConsole('INFO');
console.log = patchedConsole('INFO');
console.warn = patchedConsole('WARNING');
console.error = patchedConsole('ERROR');
