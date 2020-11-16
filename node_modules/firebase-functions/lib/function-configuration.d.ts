/**
 * List of all regions supported by Cloud Functions.
 */
export declare const SUPPORTED_REGIONS: readonly ["us-central1", "us-east1", "us-east4", "us-west2", "us-west3", "us-west4", "europe-west1", "europe-west2", "europe-west3", "europe-west6", "asia-east2", "asia-northeast1", "asia-northeast2", "asia-northeast3", "asia-south1", "asia-southeast2", "northamerica-northeast1", "southamerica-east1", "australia-southeast1"];
/**
 * Cloud Functions min timeout value.
 */
export declare const MIN_TIMEOUT_SECONDS = 0;
/**
 * Cloud Functions max timeout value.
 */
export declare const MAX_TIMEOUT_SECONDS = 540;
/**
 * List of available memory options supported by Cloud Functions.
 */
export declare const VALID_MEMORY_OPTIONS: readonly ["128MB", "256MB", "512MB", "1GB", "2GB"];
/**
 * List of available options for VpcConnectorEgressSettings.
 */
export declare const VPC_EGRESS_SETTINGS_OPTIONS: readonly ["VPC_CONNECTOR_EGRESS_SETTINGS_UNSPECIFIED", "PRIVATE_RANGES_ONLY", "ALL_TRAFFIC"];
/**
 * Scheduler retry options. Applies only to scheduled functions.
 */
export interface ScheduleRetryConfig {
    retryCount?: number;
    maxRetryDuration?: string;
    minBackoffDuration?: string;
    maxBackoffDuration?: string;
    maxDoublings?: number;
}
/**
 * Configuration options for scheduled functions.
 */
export interface Schedule {
    schedule: string;
    timeZone?: string;
    retryConfig?: ScheduleRetryConfig;
}
export interface FailurePolicy {
    retry: {};
}
export declare const DEFAULT_FAILURE_POLICY: FailurePolicy;
export interface RuntimeOptions {
    /**
     * Failure policy of the function, with boolean `true` being equivalent to
     * providing an empty retry object.
     */
    failurePolicy?: FailurePolicy | boolean;
    /**
     * Amount of memory to allocate to the function.
     */
    memory?: typeof VALID_MEMORY_OPTIONS[number];
    /**
     * Timeout for the function in seconds, possible values are 0 to 540.
     */
    timeoutSeconds?: number;
    /**
     * Max number of actual instances allowed to be running in parallel
     */
    maxInstances?: number;
    /**
     * Connect cloud function to specified VPC connector
     */
    vpcConnector?: string;
    /**
     * Egress settings for VPC connector
     */
    vpcConnectorEgressSettings?: typeof VPC_EGRESS_SETTINGS_OPTIONS[number];
}
export interface DeploymentOptions extends RuntimeOptions {
    regions?: Array<typeof SUPPORTED_REGIONS[number] | string>;
    schedule?: Schedule;
}
