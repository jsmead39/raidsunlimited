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
            'addCharacter', 'loadRaidHistory'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.charactersList = [];
    }

    /**
     * Add the header to the page and load the RaidClient
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.updateProfile);
        this.header.addHeaderToPage();
        this.client = new RaidsUnlimitedClient();

        this.loadProfile();
        this.loadRaidHistory();
        this.addCharacter();
    }

    async loadProfile() {

        const urlParams = new URLSearchParams(window.location.search);
        const userId = urlParams.get('id');

        const response = await this.client.getProfile(userId);
        if (response.profileModel.characterList != null) {
            this.charactersList = response.profileModel.characterList;
        }
        this.dataStore.set('profile', response.profileModel);

        document.getElementById('email').value = response.profileModel.email;
        document.getElementById('displayName').value = response.profileModel.displayName;
        document.getElementById('warcraftLogsLink').value = response.profileModel.logs;
        // document.getElementById('warcraftLogsLink').value = "Link to Warcraft Logs";

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

        //create table body
        let tbody = characterTable.getElementsByTagName('tbody')[0];
        if (!tbody) {
            tbody = document.createElement('tbody');
            characterTable.appendChild(tbody);
        }

        if (response.profileModel.characterList != null) {
            response.profileModel.characterList.forEach(character => {
                const tr = document.createElement('tr');
                ['charName', 'charClass', 'specialization', 'role'].forEach(field => {
                    const td = document.createElement('td');
                    td.textContent = character[field];
                    tr.appendChild(td);
                });
                tbody.appendChild(tr);
            });
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
        // const email = document.getElementById('email').value;
        const displayName = document.getElementById('displayName').value;
        // const charactersList = this.charactersList;
        const logs = document.getElementById('warcraftLogsLink').value;

        try {
            const response = await this.client.updateProfile(userId, displayName, this.charactersList,
                logs);

            if (response.status === 200) {
                messageText.innerText = 'Profile successfully created';
                messageText.classList.add('success');
            }

            setTimeout(() => {
                window.location.href = 'index.html';
            }, 5000);

            messagePopup.classList.remove('hidden');
            this.dataStore.set('profile', response.data.profile);
        } catch (error) {
            console.log(error);
            changeButton.innerText = origButtonText;
            messageText.innerText = `Error: ${error.message}`;
            messageText.classList.add('error');
            messagePopup.classList.remove('hidden');
        }
    }

    submitCharacter(evt) {
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

        document.getElementById('characterFormContainer').style.display = "none";
    }

    addCharacter() {
        const addCharacterButton = document.getElementById('addCharacterButton');
        const characterFormContainer = document.getElementById('characterFormContainer');
        const characterForm = document.getElementById('characterForm');
        const characterClassDropdown = document.getElementById('characterClass');
        const characterSpecializationDropdown = document.getElementById('characterSpecialization');

        if (addCharacterButton && characterFormContainer && characterForm) {
            addCharacterButton.addEventListener('click', () => {
                characterFormContainer.style.display = "block";
            });

            characterForm.addEventListener('submit', this.submitCharacter);

            // Add an event listener for the 'keyup' event on the document
            document.addEventListener('keyup', (event) => {
                // If the 'Escape' key is pressed
                if (event.key === 'Escape' || event.key === 'Esc' || event.keyCode === 27) {
                    // Hide the form
                    document.getElementById('characterFormContainer').style.display = "none";
                }
            });

            // Update specialization options based on the selected class
            characterClassDropdown.addEventListener('change', () => {
                const selectedClass = characterClassDropdown.value;
                const specializationOptions = getSpecializationOptions(selectedClass);

                // Clear existing options
                characterSpecializationDropdown.innerHTML = '<option value="" disabled selected>Select a Specialization</option>';

                // Add new options based on the selected class
                specializationOptions.forEach(specialization => {
                    const option = document.createElement('option');
                    option.value = specialization;
                    option.textContent = specialization;
                    characterSpecializationDropdown.appendChild(option);
                });
            });

            // Function to get specialization options based on the selected class
            const getSpecializationOptions = (selectedClass) => {
                switch (selectedClass) {
                    case 'Death Knight':
                        return ['Blood', 'Frost', 'Unholy'];
                    case 'Druid':
                        return ['Balance', 'Feral', 'Restoration'];
                    case 'Hunter':
                        return ['Beast Mastery', 'Marksmanship', 'Survival'];
                    case 'Mage':
                        return ['Arcane', 'Fire', 'Frost'];
                    case 'Paladin':
                        return ['Holy', 'Protection', 'Retribution'];
                    case 'Priest':
                        return ['Discipline', 'Holy', 'Shadow'];
                    case 'Rogue':
                        return ['Assassination', 'Combat', 'Subtlety'];
                    case 'Shaman':
                        return ['Elemental', 'Enhancement', 'Restoration'];
                    case 'Warlock':
                        return ['Affliction', 'Demonology', 'Destruction'];
                    case 'Warrior':
                        return ['Arms', 'Fury', 'Protection'];
                    default:
                        return [];
                }
            };
        }
    }

    async loadRaidHistory() {
        const urlParams = new URLSearchParams(window.location.search);
        const userId = urlParams.get('id');

        const raids = await this.client.getRaidHistory(userId);
        if(raids) {
            this.populateRaidList(raids);
        }
    }

    populateRaidList(raids) {
        const raidListElement = document.getElementById('raids');

        if (raids.raidModelList.length === 0) {
            raidListElement.innerText = 'No raids found';
        } else {
            // Create a table
            const table = document.createElement('table');

            // Create table header
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            ['Raid ID', 'Raid Name', 'Server', 'Date', 'Raid Status'].forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                headerRow.appendChild(th);
            });
            thead.appendChild(headerRow);
            table.appendChild(thead);

            // Create table body
            const tbody = document.createElement('tbody');

            raids.raidModelList.forEach(raid => {
                const tr = document.createElement('tr');

                // Add raidId as a hyperlink
                const td1 = document.createElement('td');
                const a = document.createElement('a');
                a.innerText = raid.raidId;
                a.href = `viewRaid.html?id=${raid.raidId}`;
                td1.appendChild(a);
                tr.appendChild(td1);

                // Add raidName, raidServer, raidDate
                [raid.raidName, raid.raidServer, raid.raidDate, raid.raidStatus].forEach(value => {
                    const td = document.createElement('td');
                    td.textContent = value;
                    tr.appendChild(td);
                });

                tbody.appendChild(tr);
            });

            table.appendChild(tbody);
            raidListElement.appendChild(table);
        }
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userProfile = new EditProfile();
    userProfile.mount();
};

window.addEventListener('DOMContentLoaded', main);