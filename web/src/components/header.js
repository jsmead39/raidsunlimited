import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton', 'getProfileByEmail',
            'createEditProfileButton'
        ];
        this.bindClassMethods(methodsToBind, this);
        this.dataStore = new DataStore();
        this.client = new RaidsUnlimitedClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const userInfo = this.createUserInfoForHeader(currentUser);

        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        header.appendChild(await userInfo);


    }

    createSiteTitle() {
        const homeButton = document.createElement('a');
        homeButton.classList.add('header_home');
        homeButton.href = 'index.html';
        homeButton.innerText = 'RaidsUnlimited';

        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        siteTitle.appendChild(homeButton);

        return siteTitle;
    }

     createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        if (currentUser) {
            const logoutButton = this.createLogoutButton(currentUser);
            userInfo.appendChild(logoutButton);

            // Try to get the user's profile by their email.
            //
            this.client.getProfileByEmail(currentUser.email).then(profile => {
                if (profile && profile.profileModel.userId) {
                    // The user has a profile. Create the "edit profile" button.
                    const editProfileButton = this.createEditProfileButton(profile.profileModel.userId);
                    userInfo.appendChild(editProfileButton);
                    this.dataStore.set('profileModel', profile.profileModel);

                }
            });
        } else {
            const loginButton = this.createLoginButton();
            userInfo.appendChild(loginButton);
        }

        return userInfo;
    }


    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }

    async getProfileByEmail(email) {
        try {
            const profile = await this.client.getProfileByEmail(email);
            return profile;
        } catch (error) {
            console.error("Failed to retrieve profile", error);
            return null;
        }
    }

    createEditProfileButton(userId) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = `/editProfile.html?id=${userId}`;
        button.innerText = "Edit Profile";
        return button;
    }
}
