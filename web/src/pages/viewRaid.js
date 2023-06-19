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
            'handleCharacterSelection', 'deleteRaidEvent', 'confirmUser', 'removeUser', 'redirectToEditRaid',
            'changeSignupButton', 'withdrawEvent', 'leaveFeedback', 'feedbackForm', 'getFeedback'], this);
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

        document.getElementById('delete-btn').addEventListener('click', this.deleteRaidEvent);
        document.getElementById('edit-btn').addEventListener('click', this.redirectToEditRaid);
        document.getElementById('feedback-btn').addEventListener('click', this.feedbackForm);
        document.getElementById('viewfeedback-btn').addEventListener('click', this.getFeedback);


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
            let buttonClass = participant.participantStatus ? 'remove-btn' : 'confirm-btn';
            let buttonText = participant.participantStatus ? 'Remove' : 'Confirm';
            let displayNameCapitalized = participant.displayName.charAt(0).toUpperCase() + participant.displayName.slice(1);
            participantHtml += `
                <tr>
                    <td><a href="viewProfile.html?id=${participant.userId}">${displayNameCapitalized}</a></td>
                    <td>${participant.participantClass}</td>
                    <td>${participant.participantSpecialization}</td>
                    <td>${participant.role}</td>
                    <td>${participant.participantStatus ? 'Confirmed' : 'Not Confirmed'}</td>
                     <td>
                        <button class="${buttonClass}" data-userid="${participant.userId}" 
                        data-raidid="${raidModel.raidId}" data-role="${participant.role}">${buttonText}</button>
                     </td>             
                </tr>
                `;
        }
        participantTableBody.innerHTML += participantHtml;

        //event listener for confirmation
        Array.from(document.getElementsByClassName('confirm-btn')).forEach((button) => {
            button.addEventListener('click', this.confirmUser);
        });

        Array.from(document.getElementsByClassName('remove-btn')).forEach((button) => {
            button.addEventListener('click', this.removeUser);
        });
        this.changeSignupButton();
        document.querySelector('.card').classList.remove('hidden');

    }

    async confirmUser(event) {
        const userId = event.target.getAttribute('data-userid');
        const raidId = event.target.getAttribute('data-raidid');
        const role = event.target.getAttribute('data-role');
        let statusCell = event.target.parentElement.parentElement.children[4];
        statusCell.innerText = 'Processing...';

        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        let response;

        try {
            response = await this.client.roleAssignment(raidId, userId, role, (error) => {
                messageText.innerText = 'An error occurred when confirming';
                messageText.classList.add('error');
                messagePopup.classList.remove('hidden');
                console.error(`Error: ${error.message}`);

                setTimeout(() => {
                    messagePopup.classList.add('hidden');
                    this.clientLoaded();
                }, 3000);  // Delay of 3 seconds
            });
            console.log("response in confirm", response);
            if (response.status === true) {
                statusCell.innerText = 'Confirmed';
                event.target.innerText = 'Remove';
                event.target.classList.remove('confirm-btn');
                event.target.classList.add('remove-btn');
                event.target.removeEventListener('click', this.confirmUser);
                event.target.addEventListener('click', this.removeUser)
            } else {
                statusCell.innerText = 'Not confirmed';
            }
            messagePopup.classList.add('hidden');
        } catch (error) {
            // Do nothing here as error is handled in error callback
        }
    }

    async removeUser(event) {
        const userId = event.target.getAttribute('data-userid');
        const raidId = event.target.getAttribute('data-raidid');
        let statusCell = event.target.parentElement.parentElement.children[4];
        statusCell.innerText = 'Processing...';

        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        let response;

        try {
            response = await this.client.roleRemoval(raidId, userId, (error) => {
                messageText.innerText = 'An error occurred when removing';
                messageText.classList.add('error');
                messagePopup.classList.remove('hidden');

                setTimeout(() => {
                    messagePopup.classList.add('hidden');
                    this.clientLoaded();
                }, 5000);  // Delay of 5 seconds
            });

            if (response.status === false) {
                statusCell.innerText = 'Not Confirmed';
                event.target.innerText = 'Confirm';
                event.target.classList.remove('remove-btn');
                event.target.classList.add('confirm-btn');
                event.target.removeEventListener('click', this.removeUser);
                event.target.addEventListener('click', this.confirmUser);
            } else {
                statusCell.innerText = 'Confirmed';
            }
            messagePopup.classList.add('hidden');
        } catch (error) {
            // Do nothing here as error is handled in error callback
        }
    }


    displayCharacters() {
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
        // dropdown.style.left = event.pageX + 'px';
        // dropdown.style.top = event.pageY + 'px';
        dropdown.style.display = 'block';

        // Add an event listener for the 'keyup' event on the document
        document.addEventListener('keyup', (event) => {
            // If the 'Escape' key is pressed
            if (event.key === 'Escape' || event.key === 'Esc' || event.keyCode === 27) {
                // Hide the form
                dropdown.style.display = 'none';
            }
        });
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

        let raid;

        try {
            dropdown.style.display = 'none';
            raid = await this.client.raidSignup(raidId, userId, displayName, character, (error) => {
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
                }, 3000);  // Delay of 5 seconds
            });
            this.dataStore.set('raid', raid);

            if (raid) {
                messageText.innerText = 'Raid signup successful.';
                messageText.classList.add('success');
                messagePopup.classList.remove('hidden');

                setTimeout(() => {
                    messageText.innerText = '';
                    messagePopup.classList.add('hidden');
                    this.clientLoaded();
                }, 3000);  // Delay of 5 seconds
            }
        } catch (error) {

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

        let response;

        try {
            response = await this.client.deleteRaidEvent(raidId, (error) => {
                messageText.innerText = `${error.message}`;
                messageText.classList.add('error');
                messagePopup.classList.remove('hidden');

                setTimeout(() => {
                    messagePopup.classList.add('hidden');
                }, 5000);
            });

            if (response) {
                messageText.innerText = "Raid event was successfully deleted.";
                messageText.classList.add('success');
                messagePopup.classList.remove('hidden');

                setTimeout(() => {
                    messagePopup.classList.add('hidden');
                    window.location.href = "index.html";
                }, 5000);
            }
        } catch (error) {

        }
    }

    redirectToEditRaid() {
        const raid = this.dataStore.get('raid');
        console.log("raid in redirect", raid);
        const raidId = raid.raidId;

        if (raid != null) {
            window.location.href = `/createRaid.html?id=${raidId}`;
        }
    }

    changeSignupButton() {
        const signUpButton = document.getElementById('signup-btn');
        const raidModel = this.dataStore.get('raid');
        const profileModel = this.header.dataStore.get('profileModel');
        console.log("profile model in change signup");
        const userId = profileModel.userId;


        this.displayCharactersHandler = (event) => this.displayCharacters(event);

        if (raidModel && profileModel) {
            const isParticipant = raidModel.participants.some(participant => participant.userId === userId);
            if (isParticipant) {
                signUpButton.innerText = 'Withdraw';
                signUpButton.removeEventListener('click', this.displayCharactersHandler);
                signUpButton.addEventListener('click', this.withdrawEvent);
            } else {
                signUpButton.innerText = 'Sign Up';
                signUpButton.removeEventListener('click', this.withdrawEvent);
                signUpButton.addEventListener('click', this.displayCharactersHandler)
            }
        }
    }

    async withdrawEvent() {
        const characterDropdown = document.getElementById('character-dropdown');
        characterDropdown.style.display = 'none';
        const raidModel = this.dataStore.get('raid');
        const raidId = raidModel.raidId;
        const userId = this.header.dataStore.get('profileModel').userId;

        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        let response;

        try {
            response = await this.client.raidWithdraw(raidId, userId, (error) => {
                messageText.innerText = `Error: ${error.message}`;
                messagePopup.classList.remove('hidden');
            });

            if (response.status === 200) {
                messageText.innerText = "You have been withdraw from the raid event.";
                messagePopup.classList.add('success');
                messagePopup.classList.remove('hidden');
            }


            setTimeout(() => {
                this.clientLoaded();
                messagePopup.classList.add('hidden');
                // window.location.href = "index.html";
            }, 3000);
        } catch (error) {

        }
    }

    /**
     * Configures the feedback form. It removes any existing event listener from the
     * 'Save' button to prevent multiple event handlers from being attached, and then
     * re-attaches the event listener for submitting feedback.
     *
     * It also resets the rating and comment fields in the form to their default values.
     *
     * @function feedbackForm
     * @returns {void}
     */
    feedbackForm() {
        const feedbackSave = document.getElementById('feedback-save');
        feedbackSave.removeEventListener('click', this.leaveFeedback);
        feedbackSave.addEventListener('click', this.leaveFeedback);

        const ratingReset = document.getElementById('rating');
        const commentReset = document.getElementById('comment');

        ratingReset.value = '5';
        commentReset.value = '';
    }

    /**
     * Submits feedback for a particular raid.
     * It retrieves the raid and user details from the dataStore and feedback details from the form.
     * It then calls the `createFeedback` method on the client to send the feedback to the server.
     * The response from the server is checked - if successful, a success message is shown; if not, the error message is shown.
     * After displaying a success message, the message is hidden after 3 seconds.
     *
     * @async
     * @function leaveFeedback
     * @throws Will throw an error if the feedback submission fails
     * @returns {void}
     */
     async leaveFeedback() {
        const raidModel = this.dataStore.get('raid');
        const raidId = raidModel.raidId;
        const userId = this.header.dataStore.get('profileModel').userId;
        const rating = document.getElementById('rating').value;
        const comments = document.getElementById('comment').value;

        const messagePopup = document.getElementById('messagePopup');
        const messageText = document.getElementById('messageText');

        let response;

        try {
            response = await this.client.createFeedback(userId, raidId, rating, comments, (error) => {
                messageText.innerText = `${error.message}`;
                messageText.classList.add('error');
                messagePopup.classList.remove('hidden');
            });

            if (response.status === 200) {
                messageText.innerText = "Feedback submitted.";
                messagePopup.classList.add('success');
                messagePopup.classList.remove('hidden');
            }

            setTimeout(() => {
                messagePopup.classList.add('hidden');
            }, 3000);
        } catch (error) {

        }
    }

    /**
     * Retrieves the raid model from the data store and generates HTML to populate
     * the feedback modal. It loops through each feedback in the raid model and creates
     * a new table row for each feedback. It creates and populates a table cell for both
     * the rating and comments, then appends these cells to the row. Finally, it appends
     * the row to the feedback list in the modal.
     * @function getFeedback
     * @returns {void}
     */
    getFeedback() {
        const raidModel = this.dataStore.get('raid');
        console.log(raidModel);

        // Get the feedback array from the raidModel
        const feedbacks = raidModel.feedback;

        // Get a reference to the feedback-list in the modal
        const feedbackList = document.getElementById('feedback-list');

        // Clear existing feedbacks in the list
        feedbackList.innerHTML = '';

        // Loop through each feedback
        feedbacks.forEach(feedback => {
            // Create a table row
            const row = document.createElement('tr');

            // Create a cell for the rating and set its text
            const ratingCell = document.createElement('td');
            ratingCell.innerText = feedback.rating;
            row.appendChild(ratingCell);

            // Create a cell for the comments and set its text
            const commentCell = document.createElement('td');
            commentCell.innerText = feedback.comments;
            row.appendChild(commentCell);

            // Append the row to the feedback list
            feedbackList.appendChild(row);
        });
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




