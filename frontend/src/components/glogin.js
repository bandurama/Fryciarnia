

import { GoogleLogin } from "@react-oauth/google";


export default function GLogin ()
{
    const onSuccess = function (e)
    {
        console.log('success', e);
    }

    const onError = function (e)
    {
        console.log('failure', e);
    }

    return (
        <div id="signInButton">
            <GoogleLogin
                onSuccess={onSuccess}
                onError={onError}
                cookiePolicy={'none'}
                hostedDomain={'bandurama.ddns.net'}
                redirectUri={'http://bandurama.ddns.net:3000'}
            />
        </div>
    )
}