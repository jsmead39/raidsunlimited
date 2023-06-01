import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the edit profile page of the website.
 */
class EditProfile extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['loadProfile', 'mount', 'addCharacter', 'updateProfile', 'submitCharacter',
            'addCharacter'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.charactersList = [];
    }

    /**
     * Add the header to the page and load the RaidClient
     */
    async mount() {
        await this.header.addHeaderToPage();
        this.client = new RaidsUnlimitedClient();
        await this.loadProfile();
    }

    async loadProfile() {

        const urlParams = new URLSearchParams(window.location.search);
        const userId = urlParams.get('id');

        const response = await this.client.getProfile(userId);
        this.charactersList = response.charactersList;
        this.dataStore.set('profile', response);

        document.getElementById('email').innerText = response.email;
        document.getElementById('displayName').innerText = response.displayName;
        document.getElementById('warcraftLink').href = response.logs;
        document.getElementById('warcraftLogsLink').innerText = "Link to Warcraft Logs";


        //need to create a table here if it doesn't exist
        let charactersTable = document.getElementById('characterTable');
        if (!charactersTable) {
            charactersTable = document.createElement('table');
            charactersTable.setAttribute('id', 'characterTable');

            // noinspection DuplicatedCode
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            ['Name', 'Class', 'Specialization', 'Role'].forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                headerRow.appendChild(th);
            });
            thead.appendChild(headerRow);
            charactersTable.appendChild(thead)
            document.getElementById('characterList').appendChild(charactersTable);

        }
    }

    async updateProfile(evt) {
        evt.preventDefault();

        const changeButton = document.getElementById('create');
        const origButtonText = changeButton.innerText;
        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        const profileData = this.dataStore.get('profile');

        const userId = profileData.userId;
        const email = document.getElementById('email').value;
        const displayName = document.getElementById('displayName').value;
        const charactersList = this.charactersList;
        const logs = document.getElementById('warcraftLogsLink').value;
    }

    async submitCharacter(evt) {
        evt.preventDefault();

        const charName = document.getElementById('characterName').value;
        const charClass = document.getElementById('characterClass').value;
        const specialization = document.getElementById('characterSpecialization').value;
        const role = document.getElementById('characterRole').value;

        //need to create a table here if it doesn't exist
        let characterTable = document.getElementById('characterTable');
        if (!characterTable) {
            characterTable = document.createElement('table');
            characterTable.setAttribute('id', 'characterTable');
            // noinspection DuplicatedCode
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            ['Name', 'Class', 'Specialization', 'Role'].forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                headerRow.appendChild(th);
            });
            thead.appendChild(headerRow);
            characterTable.appendChild(thead)
            document.getElementById('characterList').appendChild(characterTable);

        }

        //Add a character to our table
        const tbody = characterTable.querySelector('tbody') ||
            characterTable.appendChild(document.createElement('tbody'));
        const dataRow = document.createElement('tr');
        [charName, charClass, specialization, role].forEach(value => {
            const td = document.createElement('td');
            td.textContent = value;
            dataRow.appendChild(td);
        });
        tbody.appendChild(dataRow);


        this.charactersList.push({
            charName: charName,
            charClass: charClass,
            specialization: specialization,
            role: role
        });

        document.getElementById('characterName').value = '';
        document.getElementById('characterClass').value = '';
        document.getElementById('characterSpecialization').value = '';
        document.getElementById('characterRole').value = '';

        document.getElementById('characterFormContainer').classList.add('hidden');
    }

    addCharacter() {
        const addCharacterButton = document.getElementById('addCharacterButton');
        const characterFormContainer = document.getElementById('characterFormContainer');
        const characterForm = document.getElementById('characterForm');

        if (addCharacterButton && characterFormContainer && characterForm) {
            addCharacterButton.addEventListener('click', () => {
                characterFormContainer.classList.remove('hidden');
            });

            characterForm.addEventListener('submit', this.submitCharacter);
        }
    }


}