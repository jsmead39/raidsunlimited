import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';


/**
 * Logic needed for the User CreateProfile page of the website.
 */
class CreateProfile extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submitProfile', 'addCharacter', 'submitCharacter'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.charactersList = [];
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
        evt.preventDefault();

        const changeButton = document.getElementById('create');
        const origButtonText = changeButton.innerText;
        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        const displayName = document.getElementById('displayName').value;
        const charactersList = this.charactersList;
        const logs = document.getElementById('warcraftLogsLink').value;

        try {
            const response = await this.client.createProfile(displayName, charactersList, logs);

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
            changeButton.innerText = origButtonText;
            messageText.innerText = `Error: ${error.message}`;
            messageText.classList.add('error');
            messagePopup.classList.remove('hidden');
        }
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
}

    /**
     * Method to load the characters of the user.
     */

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userProfile = new CreateProfile();
    userProfile.mount();
};

window.addEventListener('DOMContentLoaded', main);