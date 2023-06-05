import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the homepage of the website.
 */
class Homepage extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'loadUserProfile'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.header = new Header();
        console.log("Homepage constructor");
    }

    mount() {
        this.header.addHeaderToPage();
        this.client = new RaidsUnlimitedClient();
        this.loadUserProfile();
    }

    async loadUserProfile() {
        const identity = await this.client.getIdentity();
        const email = identity.email;
        try {
            const profileSetupMessage = document.getElementById('profileSetupMessage');
            const response = await this.client.getProfileByEmail(email);
            if (response) {
                this.dataStore.set('profile', response);
            } else {
                profileSetupMessage.classList.remove('profile-hidden');
                profileSetupMessage.classList.add('profile-visible');
            }
            console.log("Userprofile in loadUserProfile", response);
        } catch (error) {
            console.error("User is not logged in to retrieve a profile", error);
        }
    }
}


const main = async () => {
    const homePage = new Homepage()
    await homePage.mount();
};

window.addEventListener('DOMContentLoaded', main);