import React, {Component, PureComponent} from 'react';
// 引入编辑器以及编辑器样式
import BraftEditor from 'braft-editor';
import 'braft-editor/dist/braft.css';

class TextEditor extends PureComponent {
  state = {
    htmlContent:null,
  };
  handleHTMLChange = (htmlContent) => {
    this.props.callback(htmlContent);
  }
  editorProps = {
    contentFormat: 'html',
    placeholder : '请输入内容!',
    onHTMLChange: this.handleHTMLChange,
    // onRawChange: this.handleHTMLChange,
  };
  render() {
    return (
      <div className="demo">
        <BraftEditor {...this.editorProps}/>
      </div>
    );
  }
}

export default TextEditor;
