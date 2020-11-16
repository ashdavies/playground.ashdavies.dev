"use strict";
var _a, _b, _c;
Object.defineProperty(exports, "__esModule", { value: true });
exports.UNPATCHED_CONSOLE = exports.CONSOLE_SEVERITY = exports.SUPPORTS_STRUCTURED_LOGS = void 0;
// Determine if structured logs are supported (node >= 10). If something goes wrong,
// assume no since unstructured is safer.
/** @hidden */
exports.SUPPORTS_STRUCTURED_LOGS = parseInt(((_c = (_b = (_a = process.versions) === null || _a === void 0 ? void 0 : _a.node) === null || _b === void 0 ? void 0 : _b.split('.')) === null || _c === void 0 ? void 0 : _c[0]) || '8', 10) >= 10;
// Map LogSeverity types to their equivalent `console.*` method.
/** @hidden */
exports.CONSOLE_SEVERITY = {
    DEBUG: 'debug',
    INFO: 'info',
    NOTICE: 'info',
    WARNING: 'warn',
    ERROR: 'error',
    CRITICAL: 'error',
    ALERT: 'error',
    EMERGENCY: 'error',
};
// safely preserve unpatched console.* methods in case of compat require
/** @hidden */
exports.UNPATCHED_CONSOLE = {
    debug: console.debug,
    info: console.info,
    log: console.log,
    warn: console.warn,
    error: console.error,
};
