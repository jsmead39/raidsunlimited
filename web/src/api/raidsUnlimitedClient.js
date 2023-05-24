import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the RaidsUnlimitedService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class RaidsUnlimitedClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'createRaid'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Create a new raid owned by the current user.
     * @param raidName The name of the raid instance.
     * @param raidServer the server of the raid.
     * @param raidDate the date of the raid.
     * @param time The time of the raid in PST.
     * @param raidSize The size of the raid.
     * @param raidObjective The objective of the raid.
     * @param lootDistribution The loot type of the raid.
     * @param requiredRoles The required roles with number values.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The raid that has been created.
     */
    async createRaid(raidName, raidServer, raidDate, time, raidSize, raidObjective,
               lootDistribution, requiredRoles, errorCallback){
        try {
            const token = await this.getTokenOrThrow("You must be logged in to create a raid.")
            const response = await this.axiosClient.post(`raidevents`, {
                raidName: raidName,
                raidServer: raidServer,
                raidDate: raidDate,
                time: time,
                raidSize: raidSize,
                raidObjective: raidObjective,
                lootDistribution: lootDistribution,
                requiredRoles: requiredRoles,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.raid;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}
