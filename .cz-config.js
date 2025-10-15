'use strict';
module.exports = {
  types: [
    { value: 'feat',        name: 'feat:        新功能 (feature)' },
    { value: 'fix',         name: 'fix:         修复bug' },
    { value: 'docs',        name: 'docs:        文档 (documentation)' },
    { value: 'style',       name: 'style:       格式 (不影响代码运行的变动)' },
    { value: 'refactor',    name: 'refactor:    重构 (即不是新增功能，也不是修改bug的代码变动)' },
    { value: 'test',        name: 'test:        增加测试' },
    { value: 'chore',       name: 'chore:       构建过程或辅助工具的变动' },
  ],
  scopes: [
    { name: 'all' },
    { name: 'annotation' },
    { name: 'apollo_starter' },
    { name: 'bean' },
    { name: 'boot' },
    { name: 'cache' },
    { name: 'cache_starter' },
    { name: 'compress' },
    { name: 'configuration' },
    { name: 'datasource_starter' },
    { name: 'ddd' },
    { name: 'doc' },
    { name: 'doc_tempalte_assembler' },
    { name: 'document' },
    { name: 'es' },
    { name: 'fsm' },
    { name: 'game' },
    { name: 'http' },
    { name: 'mybatis' },
    { name: 'orm' },
    { name: 'presistence' },
    { name: 'promise' },
    { name: 'rabbitmq_starter' },
    { name: 'redis' },
    { name: 'redis_starter' },
    { name: 'register' },
    { name: 'resource' },
    { name: 'rpc' },
    { name: 'sample' },
    { name: 'spring' },
    { name: 'test' },
    { name: 'utils' },
    { name: 'web' }
  ],
  scopeOverrides: {
    chore: [
      { name: 'all' }
    ]
  },
  allowCustomScopes: false,
  allowBreakingChanges: ['feat', 'refactor']
};
