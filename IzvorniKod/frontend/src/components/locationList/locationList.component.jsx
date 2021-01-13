import React from "react";
import './locationList.styles.scss'
import LocationMetadataContainer from "../location-metadata-container/location-metadata-container.component";
class LocationListComponent extends React.Component {
    constructor(){
        super()
    }

    render() {
        return(
            <div className = 'location-container'>
                <div className = 'location-header-container'>
                    <h3>Država</h3>
                    <h3>Grad</h3>
                    <h3>Ulica</h3>
                    <h3>Radno vrijeme od</h3>
                    <h3>Radno vrijeme do</h3>
                    <h3>Broj</h3>
                </div>
                {this.props.locations.map(location =>
                    <LocationMetadataContainer
                        country = {location.country}
                        street = {location.street}
                        city = {location.city}
                        opensAt = {location.opensAt}
                        closesAt = {location.closesAt}
                        phoneNumber = {location.phoneNumber}
                        key = {location.id}/>
                )}
            </div>
        )
    }
}

export default LocationListComponent