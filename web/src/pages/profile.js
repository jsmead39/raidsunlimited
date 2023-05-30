import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';


/**
 * Logic needed for the User Profile page of the website.
 */
class Profile extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submitProfile', 'addCharacter', 'submitCharacter', 'getCharacterList'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }

    /**
     * Add the header to the page and load the RaidClient.
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.submitProfile);

        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();

        this.addCharacter();

    }

    /**
     * Method to run when the user profile submit button is pressed. Call the RaidEventService to create the
     * raid.
     */
    async submitProfile(evt) {
        evt.preventDefault()

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const changeButton = document.getElementById('change');
        const origButtonText = changeButton.innerText;
        changeButton.innerText = 'Loading...';

        const displayName = document.getElementById('displayName').value;
        const charactersList = this.getCharacterList();
        const logs = document.getElementById('warcraftLogsLink').value;

        const profile = await this.client.createProfile(displayName, charactersList, logs, (error) => {
            changeButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('profile', profile);
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


        console.log(characterList);

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

    getCharacterList() {
        const characterList = document.getElementById('characterList');
        const rows = characterList.querySelectorAll('table');
        let charactersList = [];

        for (let i = 0; i < rows.length; i++) {
            const row = rows[i];
            const charName = row.getAttribute('date-name');
            const charClass = row.getAttribute('data-class');
            const specialization = row.getAttribute('data-specialization');
            const role = row.getAttribute('data-role');

            charactersList.push({
                charName: charName,
                charClass: charClass,
                specialization: specialization,
                role: role
            });
        }
        return charactersList;
    }
}

    /**
     * Method to load the characters of the user.
     */

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userProfile = new Profile();
    userProfile.mount();
};

window.addEventListener('DOMContentLoaded', main);