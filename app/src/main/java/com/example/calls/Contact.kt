import android.os.Parcel
import android.os.Parcelable

data class Contact(
    val key: String = "",  // Unique key
    val name: String = "",
    val phone: String = "",
    val email: String = ""
) : Parcelable {

    constructor() : this("", "", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",  // Read the key
        parcel.readString() ?: "",  // Read the name
        parcel.readString() ?: "",  // Read the phone number
        parcel.readString() ?: ""    // Read the email
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)     // Write the key
        parcel.writeString(name)    // Write the name
        parcel.writeString(phone)   // Write the phone number
        parcel.writeString(email)   // Write the email
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel) // Create a new instance from the parcel
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size) // Create an array of Contact objects
        }
    }
}