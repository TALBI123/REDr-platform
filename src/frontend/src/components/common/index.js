import { createElement } from 'react';

const h = createElement;

export const Loader = () =>
  h(
    'div',
    { className: 'flex items-center justify-center p-8' },
    h('div', { className: 'animate-spin rounded-full h-12 w-12 border-b-2 border-[#6C27D3]' })
  );

export const Toast = ({ message, type = 'info', onClose }) => {
  const bgColor = {
    success: 'bg-green-500',
    error: 'bg-red-500',
    info: 'bg-blue-500',
    warning: 'bg-yellow-500',
  }[type];

  return h(
    'div',
    { className: `${bgColor} text-white px-4 py-3 rounded-lg shadow-lg flex items-center justify-between` },
    h('span', null, message),
    onClose &&
      h(
        'button',
        { onClick: onClose, className: 'ml-4 text-lg font-bold' },
        'x'
      )
  );
};

export const Modal = ({ isOpen, title, children, onClose }) => {
  if (!isOpen) return null;

  return h(
    'div',
    { className: 'fixed inset-0 bg-black/50 flex items-center justify-center z-50' },
    h(
      'div',
      { className: 'bg-white rounded-2xl shadow-lg p-6 max-w-md w-full mx-4' },
      h(
        'div',
        { className: 'flex items-center justify-between mb-4' },
        h('h2', { className: 'text-xl font-bold text-black' }, title),
        h(
          'button',
          {
            onClick: onClose,
            className: 'text-2xl font-bold text-black/60 hover:text-black',
          },
          'x'
        )
      ),
      children
    )
  );
};
