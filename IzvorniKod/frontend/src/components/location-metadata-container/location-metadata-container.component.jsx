import React from 'react'
import './location-metadata-container.styles.scss'
import { AiOutlineEdit } from "react-icons/ai";
import { Link } from 'react-router-dom'

const LocationMetadataContainer = ({ id, country, city, street, opensAt, closesAt, phoneNumber, locationComplex }) => (
    <div>
        {locationComplex ?
            <div id='complex' className='location-metadata-container'>
                <p className='location-grid-content'>{country}</p>
                <p className='location-grid-content'>{city}</p>
                <p className='location-grid-content'>{street}</p>
                <p className='location-grid-content'>{opensAt}</p>
                <p className='location-grid-content'>{closesAt}</p>
                <p className='location-grid-content'>{phoneNumber}</p>

                <Link className='membership-grid-content' to={{
                    pathname: `/location/${id}`,
                    aboutProps: {
                        country: country,
                        city: city,
                        street: street,
                        opensAt: opensAt,
                        closesAt: closesAt,
                        phoneNumber: phoneNumber
                    }
                }}><AiOutlineEdit /></Link>
            </div>
            :
            <div id='easy' className='location-metadata-container'>
                <p className='location-grid-content'>{country}</p>
                <p className='location-grid-content'>{city}</p>
                <p className='location-grid-content'>{street}</p>
                <p className='location-grid-content'>{opensAt}</p>
                <p className='location-grid-content'>{closesAt}</p>
                <p className='location-grid-content'>{phoneNumber}</p>
            </div>
        }
    </div>
)

export default LocationMetadataContainer
