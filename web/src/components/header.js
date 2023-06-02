import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
        ];
        this.bindClassMethods(methodsToBind, this);

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
        header.appendChild(userInfo);


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

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        // let childContent;
        // if (currentUser) {
        //     childContent = this.createLogoutButton();
        //
        //     const profile = await this.getProfileByEmail(currentUser.email);
        //     if (profile && profile.profileModel.userId) {
        //         const editProfileButton = this.createEditProfileButton(profile.profileModel.userId);
        //         userInfo.appendChild(editProfileButton);
        //     }
        // } else {
        //     childContent = this.createLoginButton();
        // }

        userInfo.appendChild(childContent);
        console.log(currentUser);

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

    // async getProfileByEmail(email) {
    //     try {
    //         const profile = await this.client.getProfileByEmail(email);
    //         return profile;
    //     } catch (error) {
    //         console.error("Failed to retrieve profile", error);
    //         return null;
    //     }
    // }

    // createEditProfileButton(userId) {
    //     const button = document.createElement('a');
    //     button.classList.add('button');
    //     button.href = `/editProfile.html?id=${userId}`;
    //     button.innerText = "Edit Profile";
    //     return button;
    // }
}
