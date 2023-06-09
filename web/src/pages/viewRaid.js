import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view raid page of the website.
 */
class ViewRaid extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addRaidToPage', 'displayCharacters',
            'handleCharacterSelection', 'deleteRaidEvent'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addRaidToPage);
        this.header = new Header(this.dataStore);
    }

    /**
     * Once the client is loaded, get the raid metadata and participant list.
     */
    async clientLoaded() {
        try {
            const urlParams = new URLSearchParams(window.location.search);

            const raidId = urlParams.get('id');
            document.getElementById('raid-name').innerText = "Loading Raid ...";
            const raid = await this.client.getRaid(raidId);
            this.dataStore.set('raid', raid);
        } catch (error) {
            console.error("Error loading client data: ", error);
        }
    }

    /**
     * Add the header to the page and load the RaidClient
     */
    mount() {
        document.getElementById('signup-btn').addEventListener('click', (event) =>
            this.displayCharacters(event));

        document.getElementById('delete-btn').addEventListener('click', this.deleteRaidEvent);

        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();
        this.clientLoaded();

    }

    addRaidToPage() {
        const raidModel = this.dataStore.get('raid');
        if (raidModel == null) {
            return;
        }

        document.getElementById('raid-name').innerText = "Raid Name: " + raidModel.raidName;
        document.getElementById('raid-size').innerText = "Raid Size: " + raidModel.raidSize;
        document.getElementById('raid-date').innerText = "Raid Date: " + raidModel.raidDate;
        document.getElementById('raid-time').innerText = "Raid Time: " + raidModel.time;
        document.getElementById('raid-server').innerText = "Server: " + raidModel.raidServer;
        document.getElementById('raid-status').innerText = "Raid Status: " + raidModel.raidStatus;

        const participants = raidModel.participants;
        if (participants == null) {
            return;
        }

        const participantTableBody = document.getElementById('participant-table-body');
        participantTableBody.innerHTML = '';

        let participantHtml = '';
        let participant;
        for (participant of participants) {
            participantHtml += `
                <tr>
                    <td>${participant.displayName}</td>
                    <td>${participant.participantClass}</td>
                    <td>${participant.participantSpecialization}</td>
                    <td>${participant.role}</td>
                    
                </tr>
                `;
        }
        participantTableBody.innerHTML += participantHtml;
    }

    displayCharacters(event) {
        const profileModel = this.header.dataStore.get('profileModel');

        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        if (!profileModel) {
            messageText.innerText = 'You must be logged in and have a profile to sign up for an raid';
            messageText.classList.add('error');
            messagePopup.classList.remove('hidden');
            setTimeout(() => {
                messagePopup.classList.add('hidden');
            }, 5000);
            console.error("User does not have a profile");
            return;
        }

        const dropdown = document.getElementById('character-dropdown');
        dropdown.innerHTML = '';
        profileModel.characterList.forEach(character => {
            const characterElement = document.createElement('div');
            characterElement.innerText = `${character.charName} - ${character.charClass} - ${character.specialization} - ${character.role}`;
            characterElement.classList.add('character-option');
            characterElement.addEventListener('click', () => {
                this.handleCharacterSelection(character);
            });
            dropdown.appendChild(characterElement);
        });

        dropdown.style.left = event.clientX + 'px';
        dropdown.style.top = event.clientY + 'px';
        dropdown.style.display = 'block';
    }

    async handleCharacterSelection(character) {
        const raidModel = this.dataStore.get('raid');
        const profileModel = this.header.dataStore.get('profileModel');

        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');
        const raidId = raidModel.raidId;
        const userId = profileModel.userId;
        const displayName = profileModel.displayName;

        const dropdown = document.getElementById('character-dropdown');
        try {
            dropdown.style.display = 'none';
            const raid = await this.client.raidSignup(raidId, userId, displayName, character, (error) => {
                if (error.message.includes("already signed up")) {
                    messageText.innerText = "You are already signed up for this event";
                } else {
                    messageText.innerText = 'An error occurred when signing up';
                }
                messageText.classList.add('error');
                messagePopup.classList.remove('hidden');
                console.error(`Error: ${error.message}`);
                dropdown.style.display = 'none';

                setTimeout(() => {
                    messagePopup.classList.add('hidden');
                    this.clientLoaded();
                }, 5000);  // Delay of 5 seconds


            });
            this.dataStore.set('raid', raid);

            if(raid) {
                messageText.innerText = 'Signup successful';
                messageText.classList.add('success');
                messagePopup.classList.remove('hidden');

                setTimeout(() => {
                    messageText.innerText = '';
                    messagePopup.classList.add('hidden');
                    this.clientLoaded();
                }, 5000);  // Delay of 5 seconds
            }
        } catch (error) {
            console.error(`An unexpected error occurred: ${error.message}`);
        }
    }

    async deleteRaidEvent() {
        const raid = this.dataStore.get('raid');
        const raidId = raid.raidId;
        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');
        const confirmation = confirm('Are you sure you want to delete this event?');
        if (!confirmation) {
            return;
        }

        const response = await this.client.deleteRaidEvent(raidId, (error) => {

            if (error) {
                messageText.innerText = `${error.message}`;
                messageText.classList.add('error');
                messagePopup.classList.remove('hidden');
                console.error(`An unexpected error occurred: ${error.message}`);

                setTimeout(() => {
                    messagePopup.classList.add('hidden');
                }, 5000);
            }
        });

        if(response) {
            messageText.innerText = "Raid event was successfully deleted";
            messageText.classList.add('success');
            messagePopup.classList.remove('hidden');

            setTimeout(() => {
                messagePopup.classList.add('hidden');
                window.location.href = "index.html";
            }, 5000);
        }
    }

}

/**
 *
 * Main method to run when the page contents have loaded.
 */
const main = async() => {
    const viewRaid = new ViewRaid();
    viewRaid.mount();
};
window.addEventListener('DOMContentLoaded', main);




