
import '../styles/PopupMessageBox.css'

export default function PopupMessasgeBox ({isShown, setShown, messesage, onOk, onCancel})
{

    const renderPopup = function ()
    {
        return (
          <div className="PopupMessageBox">
            <div className="MsgBox">
              <div>
                {messesage}
              </div>
              <div>
                <button onClick={(e) => onOk(e)}>👍</button>
                <button onClick={(e) => onCancel(e)}>👎</button>
              </div>
            </div>
          </div>
        )
    }

    return isShown
        ? renderPopup()
        : <></>
}