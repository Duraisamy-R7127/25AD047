/* =========================================================
   FESTPASS FRONTEND
   ========================================================= */

const API = {
    events: "/api/events",
    attendees: "/api/attendees",
    bookings: "/api/bookings",
    tickets: "/api/tickets"
};


/* =========================================================
   GLOBAL DATA
   ========================================================= */

let events = [];
let attendees = [];
let bookings = [];
let tickets = [];


/* =========================================================
   PAGE NAVIGATION
   ========================================================= */

function showPage(page) {

    const pages = [
        "dashboard",
        "events",
        "attendees",
        "bookings",
        "tickets"
    ];

    pages.forEach(name => {

        const element =
            document.getElementById(name + "Page");

        if (element) {
            element.style.display =
                name === page ? "block" : "none";
        }

    });


    document.querySelectorAll(".nav-btn")
        .forEach(button => {

            button.classList.remove("active");

            if (button.dataset.page === page) {
                button.classList.add("active");
            }

        });


    const titles = {

        dashboard: [
            "Dashboard",
            "Manage your college fest"
        ],

        events: [
            "Events",
            "Create and manage fest events"
        ],

        attendees: [
            "Attendees",
            "Manage fest attendees"
        ],

        bookings: [
            "Bookings",
            "Manage ticket bookings"
        ],

        tickets: [
            "Tickets",
            "Tickets and QR check-in"
        ]

    };


    if (titles[page]) {

        document.getElementById("pageTitle")
            .textContent = titles[page][0];

        document.getElementById("pageSubtitle")
            .textContent = titles[page][1];

    }


    if (page === "dashboard") {
        loadDashboard();
    }

    if (page === "events") {
        loadEvents();
    }

    if (page === "attendees") {
        loadAttendees();
    }

    if (page === "bookings") {
        loadBookings();
    }

    if (page === "tickets") {
        loadTickets();
    }

}


/* =========================================================
   FETCH HELPER
   ========================================================= */

async function apiFetch(url, options = {}) {

    try {

        const response = await fetch(url, {

            ...options,

            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {})
            }

        });


        const text = await response.text();

        let data = null;

        if (text) {

            try {
                data = JSON.parse(text);
            } catch {

                data = text;

            }

        }


        if (!response.ok) {

            let message = "Server error: " + response.status;

            if (data) {

                if (typeof data === "string") {
                    message = data;
                }

                else if (data.message) {
                    message = data.message;
                }

                else if (data.error) {
                    message = data.error;
                }

                else if (data.errors) {
                    message = JSON.stringify(data.errors);
                }

            }

            throw new Error(message);
        }


        return data;

    } catch (error) {

        console.error("API ERROR:", error);

        throw error;

    }

}


/* =========================================================
   TOAST
   ========================================================= */

function showToast(message, type = "success") {

    const toast =
        document.getElementById("toast");

    toast.textContent = message;

    toast.className =
        "toast show " + type;


    setTimeout(() => {

        toast.classList.remove("show");

    }, 3500);

}


/* =========================================================
   EVENT MODAL
   ========================================================= */

function openEventModal() {

    document
        .getElementById("eventModal")
        .classList.add("show");

}


function closeEventModal() {

    document
        .getElementById("eventModal")
        .classList.remove("show");

}


/* =========================================================
   CREATE EVENT
   ========================================================= */

async function saveEvent(event) {

    event.preventDefault();


    const name =
        document.getElementById("eventName")
            .value.trim();

    const capacity =
        Number(
            document.getElementById("capacity")
                .value
        );

    const ticketPrice =
        Number(
            document.getElementById("ticketPrice")
                .value
        );

    const eventDate =
        document.getElementById("eventDate")
            .value;


    if (!name) {

        showToast(
            "Event name is required",
            "error"
        );

        return;
    }


    if (!capacity || capacity < 1) {

        showToast(
            "Capacity must be greater than 0",
            "error"
        );

        return;
    }


    if (isNaN(ticketPrice) || ticketPrice < 0) {

        showToast(
            "Enter valid ticket price",
            "error"
        );

        return;
    }


    if (!eventDate) {

        showToast(
            "Event date is required",
            "error"
        );

        return;
    }


    /*
       IMPORTANT:

       Backend expects:

       name
       capacity
       ticketPrice
       eventDate
    */

    const data = {

        name: name,

        capacity: capacity,

        ticketPrice: ticketPrice,

        eventDate: eventDate

    };


    console.log(
        "CREATE EVENT REQUEST:",
        data
    );


    try {

        await apiFetch(API.events, {

            method: "POST",

            body: JSON.stringify(data)

        });


        showToast(
            "Event created successfully!"
        );


        closeEventModal();


        document
            .querySelector("#eventModal form")
            .reset();


        await loadEvents();

        await updateDashboardStats();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   LOAD EVENTS
   ========================================================= */

async function loadEvents() {

    try {

        const data =
            await apiFetch(API.events);


        events =
            Array.isArray(data)
                ? data
                : [];


        renderEvents();

        updateDashboardStats();

    } catch (error) {

        console.error(error);

        renderEventsError(
            "Unable to load events"
        );

    }

}


/* =========================================================
   RENDER EVENTS
   ========================================================= */

function renderEvents(list = events) {

    const table =
        document.getElementById("eventsTable");


    if (!table) return;


    if (!list.length) {

        table.innerHTML = `
            <tr>
                <td colspan="6"
                    class="empty">
                    No events found
                </td>
            </tr>
        `;

        return;
    }


    table.innerHTML =
        list.map(event => {

            return `

                <tr>

                    <td>
                        #${event.id ?? "-"}
                    </td>

                    <td>
                        <strong>
                            ${escapeHtml(
                event.name ?? "-"
            )}
                        </strong>
                    </td>

                    <td>
                        ${event.capacity ?? 0}
                    </td>

                    <td>
                        ₹${formatPrice(
                event.ticketPrice ??
                event.price ??
                0
            )}
                    </td>

                    <td>
                        ${formatDate(
                event.eventDate ??
                event.startDateTime
            )}
                    </td>

                    <td>

                        <button
                            class="btn btn-red"
                            style="padding:8px 13px"
                            onclick="deleteEvent(${event.id})">
                            Delete
                        </button>

                    </td>

                </tr>

            `;

        }).join("");

}


/* =========================================================
   EVENT SEARCH
   ========================================================= */

function filterEvents() {

    const search =
        document.getElementById("eventSearch")
            .value
            .toLowerCase()
            .trim();


    const filtered =
        events.filter(event => {

            const name =
                String(event.name ?? "")
                    .toLowerCase();

            return name.includes(search);

        });


    renderEvents(filtered);

}


/* =========================================================
   DELETE EVENT
   ========================================================= */

async function deleteEvent(id) {

    if (!id) return;


    const confirmed =
        confirm(
            "Are you sure you want to delete this event?"
        );


    if (!confirmed) return;


    try {

        await apiFetch(
            `${API.events}/${id}`,
            {
                method: "DELETE"
            }
        );


        showToast(
            "Event deleted successfully"
        );


        await loadEvents();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   ATTENDEE MODAL
   ========================================================= */

function openAttendeeModal() {

    document
        .getElementById("attendeeModal")
        .classList.add("show");

}


function closeAttendeeModal() {

    document
        .getElementById("attendeeModal")
        .classList.remove("show");

}


/* =========================================================
   CREATE ATTENDEE
   ========================================================= */

async function saveAttendee(event) {

    event.preventDefault();


    const name =
        document.getElementById("attendeeName")
            .value.trim();

    const email =
        document.getElementById("attendeeEmail")
            .value.trim();

    const phone =
        document.getElementById("attendeePhone")
            .value.trim();


    if (!name || !email || !phone) {

        showToast(
            "All attendee fields are required",
            "error"
        );

        return;
    }


    const data = {

        name: name,

        email: email,

        phone: phone

    };


    try {

        await apiFetch(
            API.attendees,
            {
                method: "POST",
                body: JSON.stringify(data)
            }
        );


        showToast(
            "Attendee added successfully!"
        );


        closeAttendeeModal();


        document
            .querySelector("#attendeeModal form")
            .reset();


        await loadAttendees();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   LOAD ATTENDEES
   ========================================================= */

async function loadAttendees() {

    try {

        const data =
            await apiFetch(API.attendees);


        attendees =
            Array.isArray(data)
                ? data
                : [];


        renderAttendees();

        updateDashboardStats();


    } catch (error) {

        console.error(error);

        renderTableMessage(
            "attendeesTable",
            4,
            "Unable to load attendees"
        );

    }

}


/* =========================================================
   RENDER ATTENDEES
   ========================================================= */

function renderAttendees(list = attendees) {

    const table =
        document.getElementById(
            "attendeesTable"
        );


    if (!table) return;


    if (!list.length) {

        table.innerHTML = `
            <tr>
                <td colspan="4"
                    class="empty">
                    No attendees found
                </td>
            </tr>
        `;

        return;
    }


    table.innerHTML =
        list.map(attendee => {

            return `

                <tr>

                    <td>
                        #${attendee.id ?? "-"}
                    </td>

                    <td>
                        <strong>
                            ${escapeHtml(
                attendee.name ?? "-"
            )}
                        </strong>
                    </td>

                    <td>
                        ${escapeHtml(
                attendee.email ?? "-"
            )}
                    </td>

                    <td>
                        ${escapeHtml(
                attendee.phone ?? "-"
            )}
                    </td>

                </tr>

            `;

        }).join("");

}


/* =========================================================
   ATTENDEE SEARCH
   ========================================================= */

function filterAttendees() {

    const search =
        document.getElementById(
            "attendeeSearch"
        )
            .value
            .toLowerCase()
            .trim();


    const filtered =
        attendees.filter(attendee => {

            const name =
                String(attendee.name ?? "")
                    .toLowerCase();

            const email =
                String(attendee.email ?? "")
                    .toLowerCase();

            const phone =
                String(attendee.phone ?? "")
                    .toLowerCase();


            return (
                name.includes(search) ||
                email.includes(search) ||
                phone.includes(search)
            );

        });


    renderAttendees(filtered);

}


/* =========================================================
   BOOKING MODAL
   ========================================================= */

function openBookingModal() {

    document
        .getElementById("bookingModal")
        .classList.add("show");

}


function closeBookingModal() {

    document
        .getElementById("bookingModal")
        .classList.remove("show");

}


/* =========================================================
   CREATE BOOKING
   ========================================================= */

async function saveBooking(event) {

    event.preventDefault();


    const attendeeId =
        Number(
            document.getElementById(
                "bookingAttendeeId"
            ).value
        );


    const eventId =
        Number(
            document.getElementById(
                "bookingEventId"
            ).value
        );


    const numberOfTickets =
        Number(
            document.getElementById(
                "numberOfTickets"
            ).value
        );


    if (!attendeeId || attendeeId < 1) {

        showToast(
            "Enter valid attendee ID",
            "error"
        );

        return;
    }


    if (!eventId || eventId < 1) {

        showToast(
            "Enter valid event ID",
            "error"
        );

        return;
    }


    if (!numberOfTickets || numberOfTickets < 1) {

        showToast(
            "Enter valid ticket count",
            "error"
        );

        return;
    }


    const data = {

        attendeeId: attendeeId,

        eventId: eventId,

        numberOfTickets: numberOfTickets

    };


    console.log(
        "BOOKING REQUEST:",
        data
    );


    try {

        await apiFetch(
            API.bookings,
            {
                method: "POST",
                body: JSON.stringify(data)
            }
        );


        showToast(
            "Booking created successfully!"
        );


        closeBookingModal();


        document
            .querySelector("#bookingModal form")
            .reset();


        await loadBookings();

        await loadTickets();

        await updateDashboardStats();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   LOAD BOOKINGS
   ========================================================= */

async function loadBookings() {

    try {

        const data =
            await apiFetch(API.bookings);


        bookings =
            Array.isArray(data)
                ? data
                : [];


        renderBookings();

        updateDashboardStats();


    } catch (error) {

        console.error(error);

        renderTableMessage(
            "bookingsTable",
            5,
            "Unable to load bookings"
        );

    }

}


/* =========================================================
   RENDER BOOKINGS
   ========================================================= */

function renderBookings() {

    const table =
        document.getElementById(
            "bookingsTable"
        );


    if (!table) return;


    if (!bookings.length) {

        table.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty">
                    No bookings found
                </td>
            </tr>
        `;

        return;
    }


    table.innerHTML =
        bookings.map(booking => {

            const attendee =
                booking.attendee?.name ??
                booking.attendeeName ??
                booking.attendeeId ??
                "-";


            const event =
                booking.event?.name ??
                booking.eventName ??
                booking.eventId ??
                "-";


            const count =
                booking.numberOfTickets ??
                booking.ticketCount ??
                0;


            return `

                <tr>

                    <td>
                        #${booking.id ?? "-"}
                    </td>

                    <td>
                        ${escapeHtml(
                String(attendee)
            )}
                    </td>

                    <td>
                        ${escapeHtml(
                String(event)
            )}
                    </td>

                    <td>
                        ${count}
                    </td>

                    <td>
                        <span class="badge badge-green">
                            Confirmed
                        </span>
                    </td>

                </tr>

            `;

        }).join("");

}


/* =========================================================
   LOAD TICKETS
   ========================================================= */

async function loadTickets() {

    try {

        const data =
            await apiFetch(API.tickets);


        tickets =
            Array.isArray(data)
                ? data
                : [];


        renderTickets();

        updateDashboardStats();


    } catch (error) {

        console.error(error);

        renderTableMessage(
            "ticketsTable",
            7,
            "Unable to load tickets"
        );

    }

}


/* =========================================================
   RENDER TICKETS
   ========================================================= */

function renderTickets() {

    const table =
        document.getElementById(
            "ticketsTable"
        );


    if (!table) return;


    if (!tickets.length) {

        table.innerHTML = `
            <tr>
                <td colspan="7"
                    class="empty">
                    No tickets found
                </td>
            </tr>
        `;

        return;
    }


    table.innerHTML =
        tickets.map(ticket => {

            const checked =
                ticket.checkedIn === true;


            const qrCode =
                ticket.qrCode ??
                ticket.ticketNumber ??
                "";


            return `

                <tr>

                    <td>
                        #${ticket.id ?? "-"}
                    </td>

                    <td>
                        <strong>
                            ${escapeHtml(
                ticket.ticketNumber ??
                "-"
            )}
                        </strong>
                    </td>

                    <td>
                        ${escapeHtml(
                ticket.ticketType ??
                "General"
            )}
                    </td>

                    <td>
                        ₹${formatPrice(
                ticket.price ?? 0
            )}
                    </td>

                    <td>

                        <button
                            class="btn btn-purple"
                            style="padding:8px 12px"
                            onclick="showQRCode('${escapeJs(qrCode)}', '${escapeJs(ticket.ticketNumber ?? "")}')">
                            QR
                        </button>

                    </td>

                    <td>

                        ${
                checked

                    ?

                    `<span class="badge badge-green">
                                Checked In
                            </span>`

                    :

                    `<span class="badge badge-red">
                                Not Checked In
                            </span>`
            }

                    </td>

                    <td>

                        ${
                !checked

                    ?

                    `<button
                                class="btn btn-blue"
                                style="padding:8px 12px"
                                onclick="checkInTicket('${escapeJs(qrCode)}')">
                                Check In
                            </button>`

                    :

                    `-`
            }

                    </td>

                </tr>

            `;

        }).join("");

}


/* =========================================================
   QR CODE
   ========================================================= */

function showQRCode(qrCode, ticketNumber) {

    if (!qrCode) {

        showToast(
            "QR code is not available",
            "error"
        );

        return;
    }


    const image =
        document.getElementById(
            "qrImage"
        );


    /*
       Uses public QR generator only for
       displaying the ticket identifier.

       Your actual backend can later provide
       a generated QR image/base64.
    */

    image.src =
        "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data="
        + encodeURIComponent(qrCode);


    document.getElementById(
        "qrTicketNumber"
    ).textContent =
        ticketNumber || qrCode;


    document
        .getElementById("qrModal")
        .classList.add("show");

}


function closeQRModal() {

    document
        .getElementById("qrModal")
        .classList.remove("show");

}


/* =========================================================
   QR CHECK-IN
   ========================================================= */

async function checkInTicket(qrCode) {

    if (!qrCode) {

        showToast(
            "Invalid QR code",
            "error"
        );

        return;
    }


    const confirmed =
        confirm(
            "Check-in this ticket?"
        );


    if (!confirmed) return;


    try {

        await apiFetch(
            `${API.tickets}/checkin/${encodeURIComponent(qrCode)}`,
            {
                method: "POST"
            }
        );


        showToast(
            "Ticket checked in successfully!"
        );


        await loadTickets();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   DASHBOARD
   ========================================================= */

async function loadDashboard() {

    await Promise.allSettled([

        loadEvents(),

        loadAttendees(),

        loadBookings(),

        loadTickets()

    ]);


    updateDashboardStats();

}


/* =========================================================
   DASHBOARD STATS
   ========================================================= */

function updateDashboardStats() {

    const totalEvents =
        document.getElementById(
            "totalEvents"
        );


    const totalAttendees =
        document.getElementById(
            "totalAttendees"
        );


    const totalBookings =
        document.getElementById(
            "totalBookings"
        );


    const totalTickets =
        document.getElementById(
            "totalTickets"
        );


    if (totalEvents) {
        totalEvents.textContent =
            events.length;
    }


    if (totalAttendees) {
        totalAttendees.textContent =
            attendees.length;
    }


    if (totalBookings) {
        totalBookings.textContent =
            bookings.length;
    }


    if (totalTickets) {
        totalTickets.textContent =
            tickets.length;
    }


    renderDashboardEvents();

}


/* =========================================================
   DASHBOARD EVENTS
   ========================================================= */

function renderDashboardEvents() {

    const table =
        document.getElementById(
            "dashboardEvents"
        );


    if (!table) return;


    const recent =
        events.slice(0, 5);


    if (!recent.length) {

        table.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty">
                    No events available
                </td>
            </tr>
        `;

        return;
    }


    table.innerHTML =
        recent.map(event => {

            return `

                <tr>

                    <td>
                        #${event.id ?? "-"}
                    </td>

                    <td>
                        <strong>
                            ${escapeHtml(
                event.name ?? "-"
            )}
                        </strong>
                    </td>

                    <td>
                        ${event.capacity ?? 0}
                    </td>

                    <td>
                        ₹${formatPrice(
                event.ticketPrice ??
                event.price ??
                0
            )}
                    </td>

                    <td>
                        ${formatDate(
                event.eventDate ??
                event.startDateTime
            )}
                    </td>

                </tr>

            `;

        }).join("");

}


/* =========================================================
   HELPERS
   ========================================================= */

function formatPrice(value) {

    const number =
        Number(value);


    if (isNaN(number)) {
        return "0.00";
    }


    return number.toFixed(2);

}


function formatDate(value) {

    if (!value) {
        return "-";
    }


    try {

        const date =
            new Date(value);


        if (isNaN(date.getTime())) {
            return value;
        }


        return date.toLocaleString(
            "en-IN",
            {
                day: "2-digit",
                month: "short",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit"
            }
        );

    } catch {

        return value;

    }

}


function escapeHtml(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");

}


function escapeJs(value) {

    return String(value)
        .replaceAll("\\", "\\\\")
        .replaceAll("'", "\\'")
        .replaceAll('"', '\\"');

}


function renderTableMessage(
    id,
    columns,
    message
) {

    const table =
        document.getElementById(id);


    if (!table) return;


    table.innerHTML = `

        <tr>

            <td colspan="${columns}"
                class="empty">

                ${escapeHtml(message)}

            </td>

        </tr>

    `;

}


function renderEventsError(message) {

    renderTableMessage(
        "eventsTable",
        6,
        message
    );

}


/* =========================================================
   CLOSE MODALS WHEN CLICK OUTSIDE
   ========================================================= */

document.addEventListener(
    "click",
    function(event) {

        const overlays =
            document.querySelectorAll(
                ".modal-overlay"
            );


        overlays.forEach(overlay => {

            if (
                event.target === overlay
            ) {

                overlay.classList.remove(
                    "show"
                );

            }

        });

    }
);


/* =========================================================
   ESC KEY CLOSE
   ========================================================= */

document.addEventListener(
    "keydown",
    function(event) {

        if (event.key === "Escape") {

            document
                .querySelectorAll(
                    ".modal-overlay"
                )
                .forEach(modal => {

                    modal.classList.remove(
                        "show"
                    );

                });

        }

    }
);


/* =========================================================
   INITIAL LOAD
   ========================================================= */

document.addEventListener(
    "DOMContentLoaded",
    function() {

        console.log(
            "FestPass frontend started"
        );


        loadDashboard();

    }
);