import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the RaidsUnlimitedService.
 *
 * This could be a great place to explore Mixins. Currently, the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class RaidsUnlimitedClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'createRaid', 'getRaid',
             'createProfile', 'getProfile', 'getProfileByEmail', 'updateProfile', 'getAllRaids', 'deleteRaidEvent',
             'getRaidHistory', 'roleAssignment', 'roleRemoval'];
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
     * Create a new user profile.
     * @param displayName The display name for the user.
     * @param charactersList A list of character objects.  Each object contains the character details.
     * @param logs URL for the Warcraft Logs link.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns A user profile that has been created.
     */
    async createProfile(displayName, charactersList, logs, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("You must be logged in to create a " +
                "profile.")
            return await this.axiosClient.post('users', {
                displayName: displayName,
                charactersList: charactersList,
                logs: logs
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
        } catch (error) {
            this.handleError(error, errorCallback)
            throw error;
        }
    }

    /**
     * Updates the profile of a user asynchronously.
     *
     * @param userId         the ID of the user whose profile will be updated
     * @param displayName    the new display name for the user
     * @param charactersList the list of characters associated with the user
     * @param logs           the logs for the user
     * @param errorCallback (Optional) the callback function to handle any errors
     * @return a Promise that resolves to the updated user profile
     * @throws Exception if an error occurs during the profile update
     */
    async updateProfile(userId, displayName, charactersList, logs, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("You must be logged in to update your " +
                "profile.")
            return await this.axiosClient.put('users/{userId}', {
                userId: userId,
                displayName: displayName,
                charactersList: charactersList,
                logs: logs
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
        } catch (error) {
            this.handleError(error, errorCallback)
            throw error;
        }
    }

    /**
     * Retrieves the profile of a user asynchronously.
     *
     * @param userId         the ID of the user whose profile will be retrieved
     * @param errorCallback the callback function to handle any errors
     * @return a Promise that resolves to the user's profile data
     * @throws Exception if an error occurs during the profile retrieval
     */
    async getProfile(userId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`users/${userId}`);
            return response.data;

        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }


    /**
     * Retrieves the profile of a user by their email asynchronously.
     *
     * @param email         the email of the user whose profile will be retrieved
     * @param errorCallback the callback function to handle any errors
     * @return a Promise that resolves to the user's profile data
     *         or null if the retrieval fails
     */
    async getProfileByEmail(email, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("User is not logged in");
            const response = await this.axiosClient.get(`users/profile`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback)
            return null;
        }
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
     * Gets the raid for the given ID.
     * @param raidId Unique identifier for a raid
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The raid's metadata.
     */
    async getRaid(raidId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`raidevents/${raidId}`);
            return response.data.raidModel;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Retrieves all raid objects from the database asynchronously.
     * @param errorCallback(Optional) A function to execute if the call fails.
     * @returns All raid objects from the database.
     */
    async getAllRaids(errorCallback) {
        try {
            const response = await this.axiosClient.get(`raidevents`);
            console.log("response in get all Raids", response);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Gets the raidevents for the given UserId.
     * @param userId The userId to retrieve the raids for.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The list of raids with their details.
     */
    async getRaidHistory(userId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`users/${userId}/raids`);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Signs up a user for a raid asynchronously.
     *
     * @param raidId         the ID of the raid event for which the user is signing up
     * @param userId         the ID of the user signing up for the raid
     * @param displayName    the display name of the user signing up for the raid
     * @param gameCharacter  the game character associated with the user signing up for the raid
     * @param errorCallBack  the callback function to handle any errors
     * @return a Promise that resolves to the signup response data
     * @throws Exception if an error occurs during the signup process
     */
    async raidSignup(raidId, userId, displayName, gameCharacter, errorCallBack) {
        try {
            const token = await this.getTokenOrThrow("You must be logged in to signup for a raid.");
            const response = await this.axiosClient.post(`raidevents/${raidId}/signup`, {
                raidId: raidId,
                userId: userId,
                displayName: displayName,
                gameCharacter: gameCharacter,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallBack);

        }
    }

    /**
     * Deletes a raid event asynchronously.
     *
     * @param raidId        the ID of the raid event to be deleted
     * @param errorCallBack the callback function to handle any errors
     * @return a Promise that resolves to the deletion response data
     * @throws Exception if an error occurs during the deletion process
     */
    async deleteRaidEvent(raidId, errorCallBack) {
        try {
            const token = await this.getTokenOrThrow("You must be logged in to delete a raid.");
            const response = await this.axiosClient.delete(`raidevents/${raidId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallBack);
        }
    }


    /**
     * Assigns a role to a user for a raid asynchronously.
     *
     * @param raidId         the ID of the raid event for which the role is being assigned
     * @param userId         the ID of the user to whom the role is being assigned
     * @param raidRole       the role being assigned to the user
     * @param errorCallBack  the callback function to handle any errors
     * @return a Promise that resolves to the role assignment response data
     * @throws Exception if an error occurs during the role assignment process
     */
    async roleAssignment(raidId, userId, raidRole, errorCallBack) {
        try {
            const token = await this.getTokenOrThrow("You must be logged in to assign a role");
            const response = await this.axiosClient.post(`raidevents/${raidId}/roleassignments`, {
                userId: userId,
                raidRole: raidRole,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallBack);
        }
    }

    async roleRemoval(raidId, userId, errorCallBack) {
        try {
            const token = await this.getTokenOrThrow("You must be logged in to assign a role");
            const response = await this.axiosClient.post(`raidevents/${raidId}/roleremovals/${userId}`, {
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallBack);
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
